package com.minecraft.moonlake.economy.api.event;

import org.bukkit.event.HandlerList;

public class PlayerPointChangeEvent extends PlayerEconomyEvent {

	private final static HandlerList handlers = new HandlerList();
	private final int oldPoint;
	private final int newPoint;
	
	public PlayerPointChangeEvent(String who, int oldPoint, int newPoint) {

		super(who);
		
		this.oldPoint = oldPoint;
		this.newPoint = newPoint;
	}
	
	public int getOldPoint() {

		return this.oldPoint;
	}
	
	public int getNewPoint() {

		return this.newPoint;
	}

	@Override
	public HandlerList getHandlers() {

		return handlers;
	}
	
	public static HandlerList getHandlerList() {

        return handlers;
    }
}
