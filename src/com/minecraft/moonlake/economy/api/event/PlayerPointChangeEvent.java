package com.minecraft.moonlake.economy.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerPointChangeEvent extends PlayerEconomyEvent implements Cancellable {

	private final static HandlerList handlers = new HandlerList();
	private final int oldPoint;
	private final int newPoint;
	private boolean cancel = false;
	
	public PlayerPointChangeEvent(String who, int oldPoint, int newPoint) {
		// 构造函数
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
