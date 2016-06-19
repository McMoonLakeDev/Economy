package com.minecraft.moonlake.economy.sql;

import com.minecraft.moonlake.economy.data.PlayerEconomy;

public interface DataSource {

	/**
	 * 初始化玩家的经济数据
	 * @param name 玩家名
	 * @return 是否成功
	 */
	boolean initialization(String name);
	
	/**
	 * 是否拥有玩家的经济数据
	 * @param name 玩家名
	 * @return 是否拥有
	 */
	boolean hasPlayer(String name);
	
	/**
	 * 获取玩家的金币数据
	 * @param name 玩家名
	 * @return 金币
	 */
	double getMoney(String name);
	
	/**
	 * 获取玩家的点券数据
	 * @param name 玩家名
	 * @return 点券
	 */
	int getPoint(String name);
	
	/**
	 * 获取玩家的积分数据
	 * @param name 玩家名
	 * @return 积分
	 */
	int getScore(String name);
	
	/**
	 * 设置玩家的金币数据
	 * @param name 玩家名
	 * @param money 金币
	 * @return 是否成功
	 */
	boolean setMoney(String name, double money);
	
	/**
	 * 设置玩家的点券数据
	 * @param name 玩家名
	 * @param point 点券
	 * @return 是否成功
	 */
	boolean setPoint(String name, int point);
	
	/**
	 * 设置玩家的积分数据
	 * @param name 玩家名
	 * @param score 积分
	 * @return 是否成功
	 */
	boolean setScore(String name, int score);
	
	/**
	 * 给予玩家金币数据
	 * @param name 玩家名
	 * @param money 金币
	 * @return 是否成功
	 */
	boolean giveMoney(String name, double money);
	
	/**
	 * 给予玩家点券数据
	 * @param name 玩家名
	 * @param point 点券
	 * @return 是否成功
	 */
	boolean givePoint(String name, int point);
	
	/**
	 * 给予玩家积分数据
	 * @param name 玩家名
	 * @param score 积分
	 * @return 是否成功
	 */
	boolean giveScore(String name, int score);
	
	/**
	 * 减少玩家的金币数据
	 * @param name 玩家名
	 * @param money 金币
	 * @return 是否成功
	 */
	boolean takeMoney(String name, double money);
	
	/**
	 * 减少玩家的点券数据
	 * @param name 玩家名
	 * @param point 点券
	 * @return 是否成功
	 */
	boolean takePoint(String name, int point);
	
	/**
	 * 减少玩家的积分数据
	 * @param name 玩家名
	 * @param score 积分
	 * @return 是否成功
	 */
	boolean takeScore(String name, int score);
	
	/**
	 * 获取玩家的经济数据
	 * @param name 玩家名
	 * @return 玩家经济
	 */
	PlayerEconomy getData(String name);
}
