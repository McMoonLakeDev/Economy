package com.minecraft.moonlake.economy.listeners;

import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import com.minecraft.moonlake.economy.api.event.PlayerMoneyChangeEvent;
import com.minecraft.moonlake.economy.api.event.PlayerPointChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by MoonLake on 2016/8/2.
 */
public class EconomyListener implements Listener {

    private final MoonLakeEconomy main;

    public EconomyListener(MoonLakeEconomy main) {

        this.main = main;
    }

    public MoonLakeEconomy getMain() {

        return main;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMoney(PlayerMoneyChangeEvent event) {

        getMain().getMLogger().info("玩家 " + event.getWho() + " 的经济账户金币变动: " + event.getOldMoney() + " -> " + event.getNewMoney());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPoint(PlayerPointChangeEvent event) {

        getMain().getMLogger().info("玩家 " + event.getWho() + " 的经济账户点券变动: " + event.getOldPoint() + " -> " + event.getNewPoint());
    }
}
