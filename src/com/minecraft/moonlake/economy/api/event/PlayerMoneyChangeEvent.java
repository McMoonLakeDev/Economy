package com.minecraft.moonlake.economy.api.event;

import org.bukkit.event.HandlerList;

public class PlayerMoneyChangeEvent extends PlayerEconomyEvent {

	private final static HandlerList handlers = new HandlerList();
	private final double oldMoney;
	private final double newMoney;
	
	public PlayerMoneyChangeEvent(String who, double oldMoney, double newMoney) {

		super(who);
		
		this.oldMoney = oldMoney;
		this.newMoney = newMoney;
	}
	
	public double getOldMoney() {

		return this.oldMoney;
	}
	
	public double getNewMoney() {

		return this.newMoney;
	}
	
	@Override
	public HandlerList getHandlers() {

		return handlers;
	}
	
	public static HandlerList getHandlerList() {

        return handlers;
    }
}
