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

import com.minecraft.moonlake.api.event.MoonLakeListener;
import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.api.event.MoonLakePlayerMoneyChangeEvent;
import com.minecraft.moonlake.economy.api.event.MoonLakePlayerPointChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class EconomyListener implements MoonLakeListener {

    private final EconomyPlugin main;

    public EconomyListener(EconomyPlugin main) {
        this.main = main;
    }

    public EconomyPlugin getMain() {
        return main;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMoney(MoonLakePlayerMoneyChangeEvent event) {
        getMain().getMLogger().info("玩家 " + event.getName() + " 的经济账户金币变动: " + event.getOldMoney() + " -> " + event.getNewMoney());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPoint(MoonLakePlayerPointChangeEvent event) {
        getMain().getMLogger().info("玩家 " + event.getName() + " 的经济账户点券变动: " + event.getOldPoint() + " -> " + event.getNewPoint());
    }
}
