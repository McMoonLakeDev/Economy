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


package com.minecraft.moonlake.economy;

import com.minecraft.moonlake.MoonLakePlugin;
import com.minecraft.moonlake.api.annotation.plugin.PluginAnnotationFactory;
import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import com.minecraft.moonlake.economy.commands.CommandEconomy;
import com.minecraft.moonlake.economy.config.EconomyConfig;
import com.minecraft.moonlake.economy.listeners.EconomyListener;
import com.minecraft.moonlake.economy.listeners.PlayerListener;
import com.minecraft.moonlake.economy.vault.MoonLakeEconomyVault;
import com.minecraft.moonlake.economy.vault.MoonLakeEconomyVaultWrapped;
import com.minecraft.moonlake.event.EventHelper;
import com.minecraft.moonlake.logger.MLogger;
import com.minecraft.moonlake.logger.MLoggerWrapped;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPlugin extends JavaPlugin {

    private final MLogger mLogger;
    private MoonLakeEconomy economy;
    private MoonLakeEconomyVault economyVault;
    private EconomyConfig configuration;
    private EconomyListener economyListener;

    public EconomyPlugin() {
        this.mLogger = new MLoggerWrapped("MoonLakeEconomy");
    }

    @Override
    public void onEnable() {
        if(!setupMoonLake()) {
            this.getMLogger().error("前置月色之湖核心API插件加载失败.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.configuration = new EconomyConfig(this);
        this.configuration.reload();
        this.economy = this.economyVault = new MoonLakeEconomyVaultWrapped(this);

        if(!hookVault()) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        PluginAnnotationFactory.get().command().registerCommand(this, new CommandEconomy(this));
        EventHelper.registerEvent(new PlayerListener(this), this);

        this.reloadChangeLogger();
        this.getMLogger().info("月色之湖经济 Economy 插件 v" + getDescription().getVersion() + " 成功加载.");
    }

    @Override
    public void onDisable() {
    }

    public MLogger getMLogger() {
        return mLogger;
    }

    public EconomyConfig getConfiguration() {
        return configuration;
    }

    public MoonLakeEconomy getEconomy() {
        return economy;
    }

    public void reloadChangeLogger() {
        if(economyListener == null) {
            economyListener = new EconomyListener(this);
        }
        boolean enable = getConfiguration().isChangeLogger();

        if(enable)
            EventHelper.registerEvent(economyListener, this);
        else
            EventHelper.unregisterAll(economyListener);

        this.getMLogger().info("玩家经济账户变动日志监听器已" + (enable ? "启用" : "禁用") + ".");
    }

    private boolean setupMoonLake() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("MoonLake");
        return plugin != null && plugin instanceof MoonLakePlugin;
    }

    private boolean hookVault() {
        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            try {
                getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, economyVault, this, ServicePriority.Highest);
                getMLogger().info("成功 hook 到 Vault 经济系统.");

                return true;
            }
            catch (Exception e) {
                getMLogger().error("未成功 hook 到 Vault 经济系统.");
            }
        }
        return false;
    }
}
