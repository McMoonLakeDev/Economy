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

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class MoonLakePlayerPointChangeEvent extends MoonLakePlayerEconomyEvent {

    private final static HandlerList handlerList = new HandlerList();
    private int oldPoint;
    private int newPoint;

    public MoonLakePlayerPointChangeEvent(Player player, int oldPoint, int newPoint) throws IllegalArgumentException {
        super(player);

        this.oldPoint = oldPoint;
        this.newPoint = newPoint;
    }

    public int getOldPoint() {
        return oldPoint;
    }

    public int getNewPoint() {
        return newPoint;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
