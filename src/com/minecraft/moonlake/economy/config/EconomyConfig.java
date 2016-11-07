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


package com.minecraft.moonlake.economy.config;

import com.minecraft.moonlake.api.annotation.plugin.PluginAnnotationFactory;
import com.minecraft.moonlake.api.annotation.plugin.config.ConfigValue;
import com.minecraft.moonlake.economy.EconomyPlugin;

import java.io.File;

public class EconomyConfig {

    @ConfigValue(path = "Prefix", colorChar = '&')
    private String prefix;

    @ConfigValue(path = "MySQL.mySQLDatabase")
    private String mySQLDatabase;

    @ConfigValue(path = "MySQL.mySQLHost")
    private String mySQLHost;

    @ConfigValue(path = "MySQL.mySQLPort")
    private int mySQLPort;

    @ConfigValue(path = "MySQL.mySQLUsername")
    private String mySQLUsername;

    @ConfigValue(path = "MySQL.mySQLPassword")
    private String mySQLPassword;

    @ConfigValue(path = "MySQL.mySQLTableName")
    private String mySQLTableName;

    @ConfigValue(path = "Setting.Debug")
    private boolean debug;

    @ConfigValue(path = "Setting.Logger")
    private boolean changeLogger;

    @ConfigValue(path = "Setting.DefaultMoney")
    private double defaultMoney;

    @ConfigValue(path = "Setting.DefaultPoint")
    private int defaultPoint;

    private final EconomyPlugin main;

    public EconomyConfig(EconomyPlugin main) {
        this.main = main;
    }

    public EconomyPlugin getMain() {
        return main;
    }

    public boolean reload() {
        if(!getMain().getDataFolder().exists())
            getMain().getDataFolder().mkdirs();
        File config = new File(getMain().getDataFolder(), "config.yml");
        if(!config.exists())
            getMain().saveDefaultConfig();
        PluginAnnotationFactory.get().config().load(getMain(), "config.yml", this);

        return true;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMySQLDatabase() {
        return mySQLDatabase;
    }

    public String getMySQLHost() {
        return mySQLHost;
    }

    public int getMySQLPort() {
        return mySQLPort;
    }

    public String getMySQLUsername() {
        return mySQLUsername;
    }

    public String getMySQLPassword() {
        return mySQLPassword;
    }

    public String getMySQLTableName() {
        return mySQLTableName;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isChangeLogger() {
        return changeLogger;
    }

    public double getDefaultMoney() {
        return defaultMoney;
    }

    public int getDefaultPoint() {
        return defaultPoint;
    }
}
