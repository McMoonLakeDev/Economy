/*
 * Copyright (C) 2016 The MoonLake Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.minecraft.moonlake.economy.vault;

import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.api.PlayerEconomy;
import com.minecraft.moonlake.economy.api.event.MoonLakePlayerMoneyChangeEvent;
import com.minecraft.moonlake.economy.api.event.MoonLakePlayerPointChangeEvent;
import com.minecraft.moonlake.event.EventHelper;
import com.minecraft.moonlake.mysql.MySQLConnection;
import com.minecraft.moonlake.mysql.MySQLFactory;
import com.minecraft.moonlake.validate.Validate;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoonLakeEconomyVaultWrapped extends AbstractEconomy implements MoonLakeEconomyVault {

    private final EconomyPlugin main;

    public MoonLakeEconomyVaultWrapped(EconomyPlugin main) {
        this.main = main;
        this.initMySQL();
    }

    public EconomyPlugin getMain() {
        return main;
    }

    @Override
    public boolean isEnabled() {
        return getMain().isEnabled();
    }

    @Override
    public String getName() {
        return getMain().getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double money) {
        return money + "￥";
    }

    @Override
    public String currencyNamePlural() {
        return "￥";
    }

    @Override
    public String currencyNameSingular() {
        return "￥";
    }

    @Override
    public boolean hasAccount(String player) {
        return hasPlayer(player);
    }

    @Override
    public boolean hasAccount(String player, String world) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String player) {
        return getMoney(player);
    }

    @Override
    public double getBalance(String player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String player, double money) {
        return getBalance(player) - money >= 0;
    }

    @Override
    public boolean has(String player, String world, double money) {
        return has(player, money);
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double money) {
        double oldMoney = getMoney(player);
        boolean result = takeMoney(player, money);
        return new EconomyResponse(money, oldMoney, result ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, String world, double money) {
        return withdrawPlayer(player, money);
    }

    @Override
    public EconomyResponse depositPlayer(String player, double money) {
        double oldMoney = getMoney(player);
        boolean result = giveMoney(player, money);
        return new EconomyResponse(money, oldMoney, result ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse depositPlayer(String player, String world, double money) {
        return depositPlayer(player, money);
    }

    @Override
    public EconomyResponse createBank(String player, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String player, double money) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String player, double money) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String player, double money) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String player, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String player, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String player) {
        return initialization(player);
    }

    @Override
    public boolean createPlayerAccount(String player, String world) {
        return createPlayerAccount(player);
    }

    //
    // MoonLake Vault Implement
    ///

    private MySQLConnection connection;

    private void initMySQL() {
        this.connection = MySQLFactory.connection(
                getMain().getConfiguration().getMySQLHost(),
                getMain().getConfiguration().getMySQLPort(),
                getMain().getConfiguration().getMySQLUsername(),
                getMain().getConfiguration().getMySQLPassword()
        );
        this.initMySQLDatabase();
    }

    private void initMySQLDatabase() {
        MySQLConnection mySQLConnection = getConnection();

        try {
            String database = getMain().getConfiguration().getMySQLDatabase();

            mySQLConnection.setDatabase("mysql", true);
            mySQLConnection.createDatabase(database, true);
            mySQLConnection.setDatabase(database, true);
            mySQLConnection.dispatchStatement(
                    "create table if not exists " + getMain().getConfiguration().getMySQLTableName() + " (" +
                    "id integer not null auto_increment," +
                    "name varchar(20) not null unique," +
                    "money double not null default '0.0'," +
                    "point integer not null default '0'," +
                    "primary key (id));"
            );
        }
        catch (Exception e) {
            getMain().getMLogger().error("初始化经济数据库或数据表时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
    }

    private MySQLConnection getConnection() {
        return connection;
    }

    private void printStackTrace(Throwable throwable) {
        if(throwable == null) return;
        if(getMain().getConfiguration().isDebug())
            throwable.printStackTrace();
    }

    @Override
    public boolean initialization(String name) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);

            if(mySQLConnection.findSimpleResult("name", "select `name` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name) != null)
                return true;
            return mySQLConnection.dispatchPreparedStatement("insert into " + getMain().getConfiguration().getMySQLTableName() + " (`name`,`money`,`point`) values (?,?,?);", name,
                                                             getMain().getConfiguration().getDefaultMoney(),
                                                             getMain().getConfiguration().getDefaultPoint());
        }
        catch (Exception e){
            getMain().getMLogger().error("初始化玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public boolean hasPlayer(String name) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Map<String, Object> result = mySQLConnection.findResult("select * from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);

            return result != null && result.size() > 0;
        }
        catch (Exception e) {
            getMain().getMLogger().error("获取玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public double getMoney(String name) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("money", "select `money` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);

            if(value != null && value instanceof Number)
                return ((Number) value).doubleValue();
        }
        catch (Exception e) {
            getMain().getMLogger().error("获取玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return 0d;
    }

    @Override
    public int getPoint(String name) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("point", "select `point` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);

            if(value != null && value instanceof Number)
                return ((Number) value).intValue();
        }
        catch (Exception e) {
            getMain().getMLogger().error("获取玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return 0;
    }

    @Override
    public boolean setMoney(String name, double money) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("money", "select `money` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
            double oldMoney = 0d;

            if(value != null && value instanceof Number)
                oldMoney = ((Number) value).doubleValue();

            mySQLConnection.dispatchPreparedStatement("update " + getMain().getConfiguration().getMySQLTableName() + " set money=? where binary `name`=?;", money, name);

            MoonLakePlayerMoneyChangeEvent mpmce = new MoonLakePlayerMoneyChangeEvent(name, oldMoney, money);
            EventHelper.callEvent(mpmce);

            return true;
        }
        catch (Exception e) {
            getMain().getMLogger().warn("更新玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public boolean setPoint(String name, int point) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("point", "select `point` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
            int oldPoint = 0;

            if(value != null && value instanceof Number)
                oldPoint = ((Number) value).intValue();

            mySQLConnection.dispatchPreparedStatement("update " + getMain().getConfiguration().getMySQLTableName() + " set point=? where binary `name`=?;", point, name);

            MoonLakePlayerPointChangeEvent mppce = new MoonLakePlayerPointChangeEvent(name, oldPoint, point);
            EventHelper.callEvent(mppce);

            return true;
        }
        catch (Exception e) {
            getMain().getMLogger().warn("更新玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public boolean giveMoney(String name, double money) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("money", "select `money` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
            double oldMoney = 0d;

            if(value != null && value instanceof Number)
                oldMoney = ((Number) value).doubleValue();

            mySQLConnection.dispatchPreparedStatement("update " + getMain().getConfiguration().getMySQLTableName() + " set money=money+? where binary `name`=?;", money, name);

            MoonLakePlayerMoneyChangeEvent mpmce = new MoonLakePlayerMoneyChangeEvent(name, oldMoney, oldMoney + money);
            EventHelper.callEvent(mpmce);

            return true;
        }
        catch (Exception e) {
            getMain().getMLogger().warn("更新玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public boolean givePoint(String name, int point) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("point", "select `point` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
            int oldPoint = 0;

            if(value != null && value instanceof Number)
                oldPoint = ((Number) value).intValue();

            mySQLConnection.dispatchPreparedStatement("update " + getMain().getConfiguration().getMySQLTableName() + " set point=point+? where binary `name`=?;", point, name);

            MoonLakePlayerPointChangeEvent mppce = new MoonLakePlayerPointChangeEvent(name, oldPoint, oldPoint + point);
            EventHelper.callEvent(mppce);

            return true;
        }
        catch (Exception e) {
            getMain().getMLogger().warn("更新玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public boolean takeMoney(String name, double money) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("money", "select `money` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
            double oldMoney = 0d;

            if(value != null && value instanceof Number)
                oldMoney = ((Number) value).doubleValue();

            mySQLConnection.dispatchPreparedStatement("update " + getMain().getConfiguration().getMySQLTableName() + " set money=money-? where binary `name`=?;", money, name);

            MoonLakePlayerMoneyChangeEvent mpmce = new MoonLakePlayerMoneyChangeEvent(name, oldMoney, oldMoney - money);
            EventHelper.callEvent(mpmce);

            return true;
        }
        catch (Exception e) {
            getMain().getMLogger().warn("更新玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public boolean takePoint(String name, int point) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);
            Object value = mySQLConnection.findSimpleResult("point", "select `point` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
            int oldPoint = 0;

            if(value != null && value instanceof Number)
                oldPoint = ((Number) value).intValue();

            mySQLConnection.dispatchPreparedStatement("update " + getMain().getConfiguration().getMySQLTableName() + " set point=point-? where binary `name`=?;", point, name);

            MoonLakePlayerPointChangeEvent mppce = new MoonLakePlayerPointChangeEvent(name, oldPoint, oldPoint + point);
            EventHelper.callEvent(mppce);

            return true;
        }
        catch (Exception e) {
            getMain().getMLogger().warn("更新玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return false;
    }

    @Override
    public PlayerEconomy getData(String name) {
        Validate.notNull(name, "The player name object is null");
        MySQLConnection mySQLConnection = getConnection();

        try {
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);

            return mySQLConnection.findResult(PlayerEconomy.class, "select `name`,`money`,`point` from " + getMain().getConfiguration().getMySQLTableName() + " where binary `name`=?;", name);
        }
        catch (Exception e) {
            getMain().getMLogger().error("获取玩家 '" + name + "' 的经济数据时异常: " + e.getMessage());
            printStackTrace(e);
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
            mySQLConnection.setDatabase(getMain().getConfiguration().getMySQLDatabase(), true);

            return mySQLConnection.findResults(PlayerEconomy.class, "select `name`,`money`,`point` from " + getMain().getConfiguration().getMySQLTableName() + ";");
        }
        catch (Exception e) {
            getMain().getMLogger().error("获取玩家经济数据时异常: " + e.getMessage());
            printStackTrace(e);
        }
        finally {
            mySQLConnection.dispose();
        }
        return null;
    }
}
