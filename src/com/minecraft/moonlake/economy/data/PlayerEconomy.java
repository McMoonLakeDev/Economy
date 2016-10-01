package com.minecraft.moonlake.economy.data;

public class PlayerEconomy {

	private String name;
	private double money;
	private int point;

	public PlayerEconomy() {

	}

	public PlayerEconomy(String name, double money, int point) {

		this.name = name;
		this.money = money;
		this.point = point;
	}
	
	public String getName() {

		return name;
	}

	public double getMoney() {

		return money;
	}

	public int getPoint() {

		return point;
	}
}
