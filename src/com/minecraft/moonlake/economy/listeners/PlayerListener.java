package com.minecraft.moonlake.economy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.minecraft.moonlake.economy.Economy;

public class PlayerListener implements Listener {

	private final Economy main;
	
	public PlayerListener(Economy main) {
		// 构造函数
		this.main = main;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent event) {
		// 玩家加入事件
		Player player = event.getPlayer();
		
		// 初始化玩家的数据
		main.getEconomy().initialization(player.getName());
	}
}
