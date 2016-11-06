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


package com.minecraft.moonlake.economy.api;

import java.util.Set;

public interface MoonLakeEconomy {

    /**
     * 初始化指定玩家的经济数据
     *
     * @param name 玩家名
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean initialization(String name);

    /**
     * 是否拥有指定玩家的经济数据
     *
     * @param name 玩家名
     * @return 是否拥有
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean hasPlayer(String name);

    /**
     * 获取指定玩家的金币数据
     *
     * @param name 玩家名
     * @return 金币
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    double getMoney(String name);

    /**
     * 获取指定玩家的点券数据
     *
     * @param name 玩家名
     * @return 点券
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    int getPoint(String name);

    /**
     * 设置指定玩家的金币数据
     *
     * @param name 玩家名
     * @param money 金币
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean setMoney(String name, double money);

    /**
     * 设置指定玩家的点券数据
     *
     * @param name 玩家名
     * @param point 点券
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean setPoint(String name, int point);

    /**
     * 给予指定玩家金币数据
     *
     * @param name 玩家名
     * @param money 金币
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean giveMoney(String name, double money);

    /**
     * 给予指定玩家点券数据
     *
     * @param name 玩家名
     * @param point 点券
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean givePoint(String name, int point);

    /**
     * 减少指定玩家的金币数据
     *
     * @param name 玩家名
     * @param money 金币
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean takeMoney(String name, double money);

    /**
     * 减少指定玩家的点券数据
     *
     * @param name 玩家名
     * @param point 点券
     * @return 是否成功
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    boolean takePoint(String name, int point);

    /**
     * 获取指定玩家的经济数据
     *
     * @param name 玩家名
     * @return 玩家经济
     * @throws IllegalArgumentException 如果玩家名对象为 {@code null} 则抛出异常
     */
    PlayerEconomy getData(String name);

    /**
     * 获取数据库的玩家经济数据集合
     *
     * @return 玩家经济集合
     */
    Set<PlayerEconomy> getDatas();
}
