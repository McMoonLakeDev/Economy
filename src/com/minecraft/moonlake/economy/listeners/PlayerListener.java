package com.minecraft.moonlake.economy.listeners;

import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by MoonLake on 2016/8/2.
 */
public class PlayerListener implements Listener {

    private final MoonLakeEconomy main;

    public PlayerListener(MoonLakeEconomy main) {

        this.main = main;
    }

    public MoonLakeEconomy getMain() {

        return main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if(player != null) {

            getMain().getManager().initialization(player.getName());
        }
    }
}
