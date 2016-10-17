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
