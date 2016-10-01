package com.minecraft.moonlake.economy.api;


import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.logger.MLogger;

/**
 * Created by MoonLake on 2016/8/1.
 */
public interface MoonLakeEconomy {

    /**
     * 获取月色之湖插件主类实例对象
     *
     * @return 实例对象
     */
    EconomyPlugin getMain();

    /**
     * 获取月色之湖经济管理实例对象
     *
     * @return 管理实例对象
     */
    EconomyManager getManager();

    /**
     * 获取月色之湖控制台日志对象
     *
     * @return 日志对象
     */
    MLogger getMLogger();
}
