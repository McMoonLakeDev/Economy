package com.minecraft.moonlake.economy.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerMoneyChangeEvent extends PlayerEconomyEvent implements Cancellable {

	private final static HandlerList handlers = new HandlerList();
	private final double oldMoney;
	private final double newMoney;
	private boolean cancel = false;
	
	public PlayerMoneyChangeEvent(String who, double oldMoney, double newMoney) {
		// 构造函数
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
	public boolean isCancelled() {
		// 
		return this.cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		// 
		this.cancel = cancel;
	}
	
	@Override
	public HandlerList getHandlers() {
		// 注册事件列表
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
    	// 注册事件列表
        return handlers;
    }
}
