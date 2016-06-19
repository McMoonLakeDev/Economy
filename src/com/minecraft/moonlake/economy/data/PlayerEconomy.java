package com.minecraft.moonlake.economy.data;

public class PlayerEconomy {

	private final String player;
	private final double money;
	private final int point;
	private final int score;

	public PlayerEconomy(String name, double money, int point, int score) {
		//
		this.player = name;
		this.money = money;
		this.point = point;
		this.score = score;
	}
	
	public String getPlayer() {
		return player;
	}

	public double getMoney() {
		return money;
	}

	public int getPoint() {
		return point;
	}

	public int getScore() {
		return score;
	}
}
