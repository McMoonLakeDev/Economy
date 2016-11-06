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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerEconomy that = (PlayerEconomy) o;

        if (Double.compare(that.money, money) != 0) return false;
        if (point != that.point) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(money);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + point;
        return result;
    }

    @Override
    public String toString() {
        return "PlayerEconomy{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", point=" + point +
                '}';
    }
}
