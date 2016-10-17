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
