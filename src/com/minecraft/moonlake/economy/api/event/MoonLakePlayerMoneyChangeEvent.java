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


package com.minecraft.moonlake.economy.api.event;

import org.bukkit.event.HandlerList;

public class MoonLakePlayerMoneyChangeEvent extends MoonLakePlayerEconomyEvent {

    private final static HandlerList handlerList = new HandlerList();
    private double oldMoney;
    private double newMoney;

    public MoonLakePlayerMoneyChangeEvent(String player, double oldMoney, double newMoney) {
        super(player);

        this.oldMoney = oldMoney;
        this.newMoney = newMoney;
    }

    public double getOldMoney() {
        return oldMoney;
    }

    public double getNewMoney() {
        return newMoney;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
