package com.minecraft.moonlake.economy.api;

import com.minecraft.moonlake.economy.data.PlayerEconomy;

/**
 * Created by MoonLake on 2016/8/1.
 */
public interface EconomyManager {

    /**
     * 初始化玩家的经济数据
     *
     * @param name 玩家名
     * @return 是否成功
     */
    boolean initialization(String name);

    /**
     * 是否拥有玩家的经济数据
     *
     * @param name 玩家名
     * @return 是否拥有
     */
    boolean hasPlayer(String name);

    /**
     * 获取玩家的金币数据
     *
     * @param name 玩家名
     * @return 金币
     */
    double getMoney(String name);

    /**
     * 获取玩家的点券数据
     *
     * @param name 玩家名
     * @return 点券
     */
    int getPoint(String name);

    /**
     * 设置玩家的金币数据
     *
     * @param name 玩家名
     * @param money 金币
     * @return 是否成功
     */
    boolean setMoney(String name, double money);

    /**
     * 设置玩家的点券数据
     *
     * @param name 玩家名
     * @param point 点券
     * @return 是否成功
     */
    boolean setPoint(String name, int point);

    /**
     * 给予玩家金币数据
     *
     * @param name 玩家名
     * @param money 金币
     * @return 是否成功
     */
    boolean giveMoney(String name, double money);

    /**
     * 给予玩家点券数据
     *
     * @param name 玩家名
     * @param point 点券
     * @return 是否成功
     */
    boolean givePoint(String name, int point);

    /**
     * 减少玩家的金币数据
     *
     * @param name 玩家名
     * @param money 金币
     * @return 是否成功
     */
    boolean takeMoney(String name, double money);

    /**
     * 减少玩家的点券数据
     *
     * @param name 玩家名
     * @param point 点券
     * @return 是否成功
     */
    boolean takePoint(String name, int point);

    /**
     * 获取玩家的经济数据
     *
     * @param name 玩家名
     * @return 玩家经济
     */
    PlayerEconomy getData(String name);
}
