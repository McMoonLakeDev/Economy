package com.minecraft.moonlake.economy;

import com.minecraft.moonlake.economy.api.EconomyManager;
import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import com.minecraft.moonlake.economy.listeners.EconomyListener;
import com.minecraft.moonlake.economy.listeners.PlayerListener;
import com.minecraft.moonlake.economy.vault.EconomyVault;
import com.minecraft.moonlake.economy.wrapper.WrappedEconomy;
import com.minecraft.moonlake.logger.MLogger;
import com.minecraft.moonlake.logger.MLoggerWrapped;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by MoonLake on 2016/8/1.
 */
public class EconomyPlugin extends JavaPlugin implements MoonLakeEconomy {

    private final MLogger mLogger;
    private EconomyManager manager;
    private static MoonLakeEconomy MAIN;

    public EconomyPlugin() {

        mLogger = new MLoggerWrapped("MoonLakeEconomy");
    }

    @Override
    public void onEnable() {

        MAIN = this;

        if(!hookVault()) {

            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        manager = new WrappedEconomy(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(getInstance()), this);

        boolean log = getConfig().getBoolean("Setting.Logger");

        if(log) {

            getServer().getPluginManager().registerEvents(new EconomyListener(getInstance()), this);
        }
        mLogger.info("月色之湖经济插件 v" + getDescription().getVersion() + " 成功加载.");
    }

    @Override
    public void onDisable() {

    }

    private boolean hookVault() {

        if(getServer().getPluginManager().getPlugin("Vault") != null) {

            try {

                Economy economy = new EconomyVault(this);

                getServer().getServicesManager().register(Economy.class, economy, this, ServicePriority.Highest);

                mLogger.info("已成功 hook 到 Vault 经济系统.");

                return true;
            }
            catch (Exception e) {

                mLogger.warn("未能成功 hook 到 Vault 经济系统.");
            }
        }
        return false;
    }

    /**
     * 获取月色之湖插件实例对象
     *
     * @return 实例对象
     */
    public MoonLakeEconomy getInstance() {

        return MAIN;
    }

    /**
     * 获取月色之湖插件实例对象
     *
     * @return 实例对象
     */
    public static MoonLakeEconomy getInstances() {

        return MAIN;
    }

    /**
     * 获取月色之湖插件主类实例对象
     *
     * @return 实例对象
     */
    @Override
    public EconomyPlugin getMain() {

        return this;
    }

    /**
     * 获取月色之湖经济管理实例对象
     *
     * @return 管理实例对象
     */
    @Override
    public EconomyManager getManager() {

        return manager;
    }

    /**
     * 获取月色之湖控制台日志对象
     *
     * @return 日志对象
     */
    @Override
    public MLogger getMLogger() {

        return mLogger;
    }
}
