package com.minecraft.moonlake.economy;

import java.io.File;
import java.text.MessageFormat;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import com.minecraft.moonlake.economy.data.Setting;
import com.minecraft.moonlake.economy.listeners.PlayerListener;
import com.minecraft.moonlake.economy.util.EconomyUtil;
import com.minecraft.moonlake.economy.vault.Economy_MoonLake;

public class Economy extends JavaPlugin {

	private final ConsoleCommandSender console;
	private final PluginManager pm;
	private MoonLakeEconomy economy;
	private Setting setting;
	
	public Economy() {
		// 构造函数
		this.pm = this.getServer().getPluginManager();
		this.console = this.getServer().getConsoleSender();
	}
	
	@Override
	public void onEnable() {
		// 插件加载
		this.initFolder();
		this.setting = new Setting(this);
		this.economy = new EconomyUtil(this);
		this.pm.registerEvents(new PlayerListener(this), this);
		this.hookVault("MoonLakeEconomy", Economy_MoonLake.class);
		this.log(MessageFormat.format("月色之湖经济插件 v{0} 成功加载.", economy.getVersion()));
	}
	
	@Override
	public void onDisable() {
		// 插件卸载
	}
	
	private void initFolder() {
		// 初始化目录
		if(!getDataFolder().exists()) 
			getDataFolder().mkdir();
		File config = new File(getDataFolder(), "config.yml");
		if(!config.exists())
			saveDefaultConfig();
	}
	
	private void hookVault(String name, Class<Economy_MoonLake> emcz) {
		// 注册vault经济钩子
		try {
			net.milkbowl.vault.economy.Economy economy = emcz.getDeclaredConstructor(Economy.class).newInstance(this);
			getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, economy, this, ServicePriority.Normal);
			log("成功 hook 到 Vault 经济系统.");
		} 
		catch (Exception e) {
			// 异常
			log("未成功 hook 到 Vault 经济系统: " + e.getMessage());
		} 
	}
	
	public void log(String msg) {
		// 信息
		this.console.sendMessage("[MoonLakeEconomy] " + msg);
	}
	
	public MoonLakeEconomy getEconomy() {
		// 获取api
		return this.economy;
	}
	
	public Setting getSetting() {
		// 获取设置
		return this.setting;
	}
} 
