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


package com.minecraft.moonlake.economy.vault;

import com.minecraft.moonlake.economy.EconomyPlugin;
import com.minecraft.moonlake.economy.api.PlayerEconomy;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;
import java.util.Set;

public class MoonLakeEconomyVaultWrapped extends AbstractEconomy implements MoonLakeEconomyVault {

    private final EconomyPlugin main;

    public MoonLakeEconomyVaultWrapped(EconomyPlugin main) {
        this.main = main;
    }

    public EconomyPlugin getMain() {
        return main;
    }

    @Override
    public boolean isEnabled() {
        return getMain().isEnabled();
    }

    @Override
    public String getName() {
        return getMain().getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double money) {
        return money + "￥";
    }

    @Override
    public String currencyNamePlural() {
        return "￥";
    }

    @Override
    public String currencyNameSingular() {
        return "￥";
    }

    @Override
    public boolean hasAccount(String player) {
        return hasPlayer(player);
    }

    @Override
    public boolean hasAccount(String player, String world) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String player) {
        return getMoney(player);
    }

    @Override
    public double getBalance(String player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String player, double money) {
        return getBalance(player) - money >= 0;
    }

    @Override
    public boolean has(String player, String world, double money) {
        return has(player, money);
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double money) {
        double oldMoney = getMoney(player);
        boolean result = takeMoney(player, money);
        return new EconomyResponse(money, oldMoney, result ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, String world, double money) {
        return withdrawPlayer(player, money);
    }

    @Override
    public EconomyResponse depositPlayer(String player, double money) {
        double oldMoney = getMoney(player);
        boolean result = giveMoney(player, money);
        return new EconomyResponse(money, oldMoney, result ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse depositPlayer(String player, String world, double money) {
        return depositPlayer(player, money);
    }

    @Override
    public EconomyResponse createBank(String player, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse deleteBank(String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String player, double money) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String player, double money) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String player, double money) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String player, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String player, String world) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String player) {
        return initialization(player);
    }

    @Override
    public boolean createPlayerAccount(String player, String world) {
        return createPlayerAccount(player);
    }

    //
    // MoonLake Vault Implement
    ///

    @Override
    public boolean initialization(String name) {
        return false;
    }

    @Override
    public boolean hasPlayer(String name) {
        return false;
    }

    @Override
    public double getMoney(String name) {
        return 0;
    }

    @Override
    public int getPoint(String name) {
        return 0;
    }

    @Override
    public boolean setMoney(String name, double money) {
        return false;
    }

    @Override
    public boolean setPoint(String name, int point) {
        return false;
    }

    @Override
    public boolean giveMoney(String name, double money) {
        return false;
    }

    @Override
    public boolean givePoint(String name, int point) {
        return false;
    }

    @Override
    public boolean takeMoney(String name, double money) {
        return false;
    }

    @Override
    public boolean takePoint(String name, int point) {
        return false;
    }

    @Override
    public PlayerEconomy getData(String name) {
        return null;
    }

    @Override
    public Set<PlayerEconomy> getDatas() {
        return null;
    }
}
