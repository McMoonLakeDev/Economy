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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 玩家经济事件
 * @author MoonLake
 *
 */
public abstract class PlayerEconomyEvent extends Event {

	private final static HandlerList handlers = new HandlerList();
	private final String who;
	
	public PlayerEconomyEvent(String who) {

		this.who = who;
	}
	
	/**
	 * 获取目标玩家经济改变的名称
	 *
	 * @return 玩家名
	 */
	public final String getWho() {

		return this.who;
	}
	
	/**
	 * 获取目标玩家的Player对象
	 *
	 * @return 如果玩家没有存在 则返回 null
	 */
	public final Player getPlayer() {

		return Bukkit.getServer().getPlayer(this.who);
	}
	
	/**
	 * 获取目标玩家是否是在线状态
	 *
	 * @return 是否在线
	 */
	public final boolean isOnline() {

		Player player = getPlayer();
		return player != null && player.isOnline();
	}

	@Override
	public HandlerList getHandlers() {

		return handlers;
	}
	
	public static HandlerList getHandlerList() {

        return handlers;
    }
}
