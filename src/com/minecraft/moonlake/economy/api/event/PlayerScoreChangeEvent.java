package com.minecraft.moonlake.economy.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerScoreChangeEvent extends PlayerEconomyEvent implements Cancellable {
	
	private final static HandlerList handlers = new HandlerList();
	private final int oldScore;
	private final int newScore;
	private boolean cancel = false;
	
	public PlayerScoreChangeEvent(String who, int oldScore, int newScore) {
		// 构造函数
		super(who);
		
		this.oldScore = oldScore;
		this.newScore = newScore;
		
	}
	
	public int getOldScore() {
		return this.oldScore;
	}
	
	public int getNewScore() {
		return this.newScore;
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
