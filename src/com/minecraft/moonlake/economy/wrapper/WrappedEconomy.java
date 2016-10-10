package com.minecraft.moonlake.economy.wrapper;

import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.api.EconomyManager;
import com.minecraft.moonlake.economy.api.event.PlayerMoneyChangeEvent;
import com.minecraft.moonlake.economy.api.event.PlayerPointChangeEvent;
import com.minecraft.moonlake.economy.data.PlayerEconomy;
import com.minecraft.moonlake.mysql.MySQLConnection;
import com.minecraft.moonlake.mysql.MySQLFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * Created by MoonLake on 2016/8/1.
 */
public class WrappedEconomy implements EconomyManager {

    private final EconomyPlugin main;
    private final String database;
    private final String table;
    private final double defaultMoney;
    private final int defaultPoint;
    private final MySQLConnection mySQLConnection;

    public WrappedEconomy(EconomyPlugin main) {

        this.main = main;

        File file = new File(main.getDataFolder(), "config.yml");

        if(!file.exists()) {

            main.saveDefaultConfig();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        this.table = config.getString("MySQL.mySQLTableName");
        this.database = config.getString("MySQL.mySQLDatabase");
        this.defaultMoney = config.getDouble("Setting.DefaultMoney");
        this.defaultPoint = config.getInt("Setting.DefaultPoint");

        int port = config.getInt("MySQL.mySQLPort");
        String host = config.getString("MySQL.mySQLHost");
        String username = config.getString("MySQL.mySQLUsername");
        String password = config.getString("MySQL.mySQLPassword");

        this.mySQLConnection = MySQLFactory.connection(host, port, username, password);
        this.init();
    }

    public EconomyPlugin getMain() {

        return main;
    }

    private MySQLConnection getConnection() {

        return mySQLConnection;
    }

    private void init() {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase("mysql", true);
            mySQLConnection.createDatabase(database, true);

            mySQLConnection.setDatabase(database, true);
            mySQLConnection.dispatchStatement(

                    "create table if not exists " + table + " (" +
                    "id integer not null auto_increment," +
                    "name varchar(20) not null unique," +
                    "money double not null default '0.0'," +
                    "point integer not null default '0'," +
                    "primary key (id));"
            );
        }
        catch (Exception e) {

            getMain().getMLogger().warn("初始化月色之湖经济数据库或数据表时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
    }

    /**
     * 初始化玩家的经济数据
     *
     * @param name 玩家名
     * @return 是否成功
     */
    @Override
    public boolean initialization(String name) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            if(mySQLConnection.findSimpleResult("name", "select name from " + table + " where binary `name`=?;", name) != null) {

                return true;
            }
            return mySQLConnection.dispatchPreparedStatement("insert into " + table + " (name,money,point) values (?,?,?);", name, defaultMoney, defaultPoint);
        }
        catch (Exception e) {

            getMain().getMLogger().warn("初始化玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 是否拥有玩家的经济数据
     *
     * @param name 玩家名
     * @return 是否拥有
     */
    @Override
    public boolean hasPlayer(String name) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            Map<String, Object> result = mySQLConnection.findResult("select * from " + table + " where binary `name`=?;", name);

            return result != null && result.size() > 0;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 获取玩家的金币数据
     *
     * @param name 玩家名
     * @return 金币
     */
    @Override
    public double getMoney(String name) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            Object value = mySQLConnection.findSimpleResult("money", "select money from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Double) {

                return (double) value;
            }
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return 0d;
    }

    /**
     * 获取玩家的点券数据
     *
     * @param name 玩家名
     * @return 点券
     */
    @Override
    public int getPoint(String name) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            Object value = mySQLConnection.findSimpleResult("point", "select point from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Integer) {

                return (int) value;
            }
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return 0;
    }

    /**
     * 设置玩家的金币数据
     *
     * @param name  玩家名
     * @param money 金币
     * @return 是否成功
     */
    @Override
    public boolean setMoney(String name, double money) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            double oldMoney = 0d;
            Object value = mySQLConnection.findSimpleResult("money", "select money from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Double) {

                oldMoney = (double) value;
            }
            mySQLConnection.dispatchPreparedStatement("update set money=? where binary `name`=?;", money, name);

            PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, oldMoney, money);
            Bukkit.getServer().getPluginManager().callEvent(pmce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 设置玩家的点券数据
     *
     * @param name  玩家名
     * @param point 点券
     * @return 是否成功
     */
    @Override
    public boolean setPoint(String name, int point) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            int oldPoint = 0;
            Object value = mySQLConnection.findSimpleResult("point", "select point from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Integer) {

                oldPoint = (int) value;
            }
            mySQLConnection.dispatchPreparedStatement("update set point=? where binary `name`=?;", point, name);

            PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, oldPoint, point);
            Bukkit.getServer().getPluginManager().callEvent(ppce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 给予玩家金币数据
     *
     * @param name  玩家名
     * @param money 金币
     * @return 是否成功
     */
    @Override
    public boolean giveMoney(String name, double money) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            double oldMoney = 0d;
            Object value = mySQLConnection.findSimpleResult("money", "select money from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Double) {

                oldMoney = (double) value;
            }
            mySQLConnection.dispatchPreparedStatement("update set money=money+? where binary `name`=?;", money, name);

            PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, oldMoney, oldMoney + money);
            Bukkit.getServer().getPluginManager().callEvent(pmce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 给予玩家点券数据
     *
     * @param name  玩家名
     * @param point 点券
     * @return 是否成功
     */
    @Override
    public boolean givePoint(String name, int point) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            int oldPoint = 0;
            Object value = mySQLConnection.findSimpleResult("point", "select point from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Integer) {

                oldPoint = (int) value;
            }
            mySQLConnection.dispatchPreparedStatement("update set point=point+? where binary `name`=?;", point, name);

            PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, oldPoint, oldPoint + point);
            Bukkit.getServer().getPluginManager().callEvent(ppce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 减少玩家的金币数据
     *
     * @param name  玩家名
     * @param money 金币
     * @return 是否成功
     */
    @Override
    public boolean takeMoney(String name, double money) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            double oldMoney = 0d;
            Object value = mySQLConnection.findSimpleResult("money", "select money from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Double) {

                oldMoney = (double) value;
            }
            mySQLConnection.dispatchPreparedStatement("update set money=money-? where binary `name`=?;", money, name);

            PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, oldMoney, oldMoney - money);
            Bukkit.getServer().getPluginManager().callEvent(pmce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 减少玩家的点券数据
     *
     * @param name  玩家名
     * @param point 点券
     * @return 是否成功
     */
    @Override
    public boolean takePoint(String name, int point) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            int oldPoint = 0;
            Object value = mySQLConnection.findSimpleResult("point", "select point from " + table + " where binary `name`=?;", name);

            if(value != null && value instanceof Integer) {

                oldPoint = (int) value;
            }
            mySQLConnection.dispatchPreparedStatement("update set point=point-? where binary `name`=?;", point, name);

            PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, oldPoint, oldPoint - point);
            Bukkit.getServer().getPluginManager().callEvent(ppce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return false;
    }

    /**
     * 获取玩家的经济数据
     *
     * @param name 玩家名
     * @return 玩家经济
     */
    @Override
    public PlayerEconomy getData(String name) {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            return mySQLConnection.findResult(PlayerEconomy.class, "select name,money,point from " + table + " where binary `name`=?;", name);
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return null;
    }

    @Override
    public Set<PlayerEconomy> getDatas() {

        MySQLConnection mySQLConnection = getConnection();

        try {

            mySQLConnection.setDatabase(database, true);

            return mySQLConnection.findResults(PlayerEconomy.class, "select name,money,point from " + table + ";");
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家经济数据时异常: " + e.getMessage());
        }
        finally {

            mySQLConnection.dispose();
        }
        return null;
    }
}
