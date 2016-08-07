package com.minecraft.moonlake.economy.data;

public class PlayerEconomy {

	private final String player;
	private final double money;
	private final int point;

	public PlayerEconomy(String name, double money, int point) {

		this.player = name;
		this.money = money;
		this.point = point;
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
}
