package com.minecraft.moonlake.economy.wrapper;

import com.minecraft.moonlake.api.mysql.MySQLConnection;
import com.minecraft.moonlake.api.mysql.MySQLManager;
import com.minecraft.moonlake.api.mysql.MySQLTable;
import com.minecraft.moonlake.api.mysql.resultset.MySQLResultSet;
import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.api.EconomyManager;
import com.minecraft.moonlake.economy.api.event.PlayerMoneyChangeEvent;
import com.minecraft.moonlake.economy.api.event.PlayerPointChangeEvent;
import com.minecraft.moonlake.economy.data.PlayerEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by MoonLake on 2016/8/1.
 */
public class WrappedEconomy implements EconomyManager {

    private final EconomyPlugin main;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String table;
    private final double defaultMoney;
    private final int defaultPoint;

    public WrappedEconomy(EconomyPlugin main) {

        this.main = main;

        File file = new File(main.getDataFolder(), "config.yml");

        if(!file.exists()) {

            main.saveDefaultConfig();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        this.host = config.getString("MySQL.mySQLHost");
        this.port = config.getInt("MySQL.mySQLPort");
        this.table = config.getString("MySQL.mySQLTableName");
        this.database = config.getString("MySQL.mySQLDatabase");
        this.username = config.getString("MySQL.mySQLUsername");
        this.password = config.getString("MySQL.mySQLPassword");
        this.defaultMoney = config.getDouble("Setting.DefaultMoney");
        this.defaultPoint = config.getInt("Setting.DefaultPoint");

        this.init();
    }

    public EconomyPlugin getMain() {

        return main;
    }

    private void init() {

        Connection connection = null;
        Statement statement = null;

        try {

            MySQLConnection sqlConnection = MySQLManager.getConnection(host, port, "mysql", username, password);
            sqlConnection.queryCreate().database(database).execute();
            sqlConnection.close();

            connection = getConnection().getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(

                    "create table if not exists " + table + " (" +
                    "id integer not null auto_increment," +
                    "name varchar(20) not null unique," +
                    "money double not null default '0.0'," +
                    "point integer not null default '0'," +
                    "primary key (id));"
            );
        }
        catch (Exception e) {

            getMain().getMLogger().warn("初始化月色之湖经济数据库的数据表时异常: " + e.getMessage());
        }
        finally {

            try {

                if (connection != null) {

                    connection.close();
                }
                if (statement != null) {

                    statement.close();
                }
            } catch (Exception e) {

                getMain().getMLogger().warn("关闭数据库连接对象时异常: " + e.getMessage());
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            if(resultSet == null || !resultSet.next()) {

                table.queryInsert().field("name").field("money").field("point").value(name).value(defaultMoney).value(defaultPoint).execute();
            }
            if(resultSet != null) {

                resultSet.close();
            }
            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("初始化玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            boolean result = resultSet != null && resultSet.next();

            if(resultSet != null) {

                resultSet.close();
            }
            return result;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            double money = resultSet != null && resultSet.next() ? resultSet.getDouble("money") : 0d;

            if(resultSet != null) {

                resultSet.close();
            }
            return money;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            int point = resultSet != null && resultSet.next() ? resultSet.getInt("point") : 0;

            if(resultSet != null) {

                resultSet.close();
            }
            return point;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            table.queryUpdate().set("money", money).where("name", name).execute();

            double oldMoney = 0;

            if(resultSet != null && resultSet.next()) {

                oldMoney = resultSet.getDouble("money");
            }
            if(resultSet != null) {

                resultSet.close();
            }
            PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, oldMoney, money);
            Bukkit.getServer().getPluginManager().callEvent(pmce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            table.queryUpdate().set("point", point).where("name", name).execute();

            int oldPoint = 0;

            if(resultSet != null && resultSet.next()) {

                oldPoint = resultSet.getInt("point");
            }
            if(resultSet != null) {

                resultSet.close();
            }
            PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, oldPoint, point);
            Bukkit.getServer().getPluginManager().callEvent(ppce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            table.queryUpdate().add("money", money).where("name", name).execute();

            double oldMoney = 0;

            if(resultSet != null && resultSet.next()) {

                oldMoney = resultSet.getDouble("money");
            }
            if(resultSet != null) {

                resultSet.close();
            }
            PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, oldMoney, oldMoney + money);
            Bukkit.getServer().getPluginManager().callEvent(pmce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            table.queryUpdate().add("point", point).where("name", name).execute();

            int oldPoint = 0;

            if(resultSet != null && resultSet.next()) {

                oldPoint = resultSet.getInt("point");
            }
            if(resultSet != null) {

                resultSet.close();
            }
            PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, oldPoint, oldPoint + point);
            Bukkit.getServer().getPluginManager().callEvent(ppce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            table.queryUpdate().subtract("money", money).where("name", name).execute();

            double oldMoney = 0;

            if(resultSet != null && resultSet.next()) {

                oldMoney = resultSet.getDouble("money");
            }
            if(resultSet != null) {

                resultSet.close();
            }
            PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, oldMoney, oldMoney - money);
            Bukkit.getServer().getPluginManager().callEvent(pmce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            table.queryUpdate().subtract("point", point).where("name", name).execute();

            int oldPoint = 0;

            if(resultSet != null && resultSet.next()) {

                oldPoint = resultSet.getInt("point");
            }
            if(resultSet != null) {

                resultSet.close();
            }
            PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, oldPoint, oldPoint - point);
            Bukkit.getServer().getPluginManager().callEvent(ppce);

            return true;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("更新玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
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

        MySQLConnection connection = null;

        try {

            connection = getConnection();

            MySQLTable table = connection.getDatabase().getTable(this.table);
            MySQLResultSet resultSet = table.querySelect().where("name", name).execute();

            PlayerEconomy playerEconomy = null;

            if(resultSet != null && resultSet.next()) {

                playerEconomy = new PlayerEconomy(name, resultSet.getDouble("money"), resultSet.getInt("point"));
            }
            if(resultSet != null) {

                resultSet.close();
            }
            return playerEconomy;
        }
        catch (Exception e) {

            getMain().getMLogger().warn("获取玩家 " + name + " 的经济数据时异常: " + e.getMessage());
        }
        finally {

            if(connection != null) {

                connection.close();
            }
        }
        return null;
    }

    private MySQLConnection getConnection() {

        MySQLConnection connection = MySQLManager.getConnection(host, port, database, username, password);
        connection.open();

        return connection;
    }
}
