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


package com.minecraft.moonlake.economy.commands;

import com.minecraft.moonlake.api.annotation.plugin.command.Command;
import com.minecraft.moonlake.api.annotation.plugin.command.CommandArgument;
import com.minecraft.moonlake.api.annotation.plugin.command.CommandArgumentOptional;
import com.minecraft.moonlake.api.annotation.plugin.command.CommandPermission;
import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.api.EconomyType;
import com.minecraft.moonlake.manager.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEconomy {

    private final EconomyPlugin main;

    public CommandEconomy(EconomyPlugin main) {
        this.main = main;
    }

    public EconomyPlugin getMain() {
        return main;
    }

    @Command(name = "meconomy", usage = "<help|reload>", min = 1)
    @CommandPermission(value = "moonlake.economy")
    public void onCommand(CommandSender sender, @CommandArgument String arg) {
        if(arg.equalsIgnoreCase("help")) {
            sender.sendMessage(new String[] {
                    "/meconomy help - 查看命令帮助.",
                    "/meconomy reload - 重新载入配置文件.",
                    "/meconomy-set <money|point> <值> [玩家] - 设置指定玩家的账户经济数据.",
                    "/meconomy-give <money|point> <值> [玩家] - 给予指定玩家的账户经济数据.",
                    "/meconomy-take <money|point> <值> [玩家] - 减少指定玩家的账户经济数据.",
            });
        }
        else if(arg.equalsIgnoreCase("reload")) {
            if(getMain().getConfiguration().reload()) {
                getMain().reloadChangeLogger();
                sender.sendMessage(getMain().getConfiguration().getPrefix() + "配置文件已经重新载入...");
            }
        }
        else {
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "未知的命令参数.");
        }
    }

    @Command(name = "meconomy-set", usage = "<money|point> <值> [玩家]", min = 2, max = 3)
    @CommandPermission(value = "moonlake.economy")
    public void onSet(CommandSender sender, @CommandArgument String type, @CommandArgument String value, @CommandArgumentOptional String player) {
        handlerOption(sender, type, value, player, "set");
    }

    @Command(name = "meconomy-give", usage = "<money|point> <值> [玩家]", min = 2, max = 3)
    @CommandPermission(value = "moonlake.economy")
    public void onGive(CommandSender sender, @CommandArgument String type, @CommandArgument String value, @CommandArgumentOptional String player) {
        handlerOption(sender, type, value, player, "give");
    }

    @Command(name = "meconomy-take", usage = "<money|point> <值> [玩家]", min = 2, max = 3)
    @CommandPermission(value = "moonlake.economy")
    public void onTake(CommandSender sender, @CommandArgument String type, @CommandArgument String value, @CommandArgumentOptional String player) {
        handlerOption(sender, type, value, player, "take");
    }

    private void handlerOption(CommandSender sender, String type, String value, String player, String option) {
        if(!(sender instanceof Player) && player == null) {
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "控制台必须填入 [玩家] 参数.");
            return;
        }
        EconomyType economyType = EconomyType.fromType(type);

        if(economyType == null) {
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "错误的经济数据类型: " + type);
            return;
        }
        double valueAmount = 0d;

        try {
            valueAmount = Double.parseDouble(value);
        }
        catch (Exception e) {
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "错误的经济数据值: " + value + " (应为数字)");
            return;
        }
        String target = player != null ? player : sender.getName();

        if(!target.matches("([0-9a-zA-Z_]{3,16})")) {
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "错误的玩家名格式: " + target + " (应为 [0-9a-zA-Z_] 3-16 字符)");
            return;
        }
        if(PlayerManager.fromName(target) == null) {
            getMain().getEconomy().initialization(target);
        }
        if(option.equals("set")) {
            if(economyType == EconomyType.MONEY)
                getMain().getEconomy().setMoney(target, valueAmount);
            else
                getMain().getEconomy().setPoint(target, (int) valueAmount);
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "成功将玩家 " + target + " 的" + economyType.getDisplayName() + "设置为 " + String.valueOf(economyType == EconomyType.MONEY ? valueAmount : (int) valueAmount));
        }
        else if(option.equals("give")) {
            if(economyType == EconomyType.MONEY)
                getMain().getEconomy().giveMoney(target, valueAmount);
            else
                getMain().getEconomy().givePoint(target, (int) valueAmount);
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "成功将玩家 " + target + " 的" + economyType.getDisplayName() + "给予 " + String.valueOf(economyType == EconomyType.MONEY ? valueAmount : (int) valueAmount));
        }
        else if(option.equals("take")) {
            if(economyType == EconomyType.MONEY)
                getMain().getEconomy().takeMoney(target, valueAmount);
            else
                getMain().getEconomy().takePoint(target, (int) valueAmount);
            sender.sendMessage(getMain().getConfiguration().getPrefix() + "成功将玩家 " + target + " 的" + economyType.getDisplayName() + "减少 " + String.valueOf(economyType == EconomyType.MONEY ? valueAmount : (int) valueAmount));
        }
    }
}
