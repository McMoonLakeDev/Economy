package com.minecraft.moonlake.economy.vault;

import com.minecraft.moonlake.economy.EconomyPlugin;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

/**
 * Created by MoonLake on 2016/8/1.
 */
public class EconomyVault extends AbstractEconomy {

    private final EconomyPlugin main;

    public EconomyVault(EconomyPlugin main) {

        this.main = main;
    }

    public final EconomyPlugin getMain() {
        
        return this.main;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double money) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double money) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double money) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public EconomyResponse createBank(String name, String player) {
        
        return createBank(name, Bukkit.getServer().getOfflinePlayer(name));
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean createPlayerAccount(String name) {
        
        return createPlayerAccount(Bukkit.getServer().getOfflinePlayer(name));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean createPlayerAccount(String name, String world) {
        
        return createPlayerAccount(Bukkit.getServer().getOfflinePlayer(name), world);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        
        return true;
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
    public EconomyResponse deleteBank(String name) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public EconomyResponse depositPlayer(String name, double money) {

        return depositPlayer(Bukkit.getServer().getOfflinePlayer(name), money);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double money) {

        double oldBalance = getBalance(player);
        boolean result = getMain().getManager().giveMoney(player.getName(), money);
        return new EconomyResponse(money, oldBalance, result ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public EconomyResponse depositPlayer(String name, String world, double money) {
        
        return depositPlayer(Bukkit.getServer().getOfflinePlayer(name), world, money);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double money) {
        
        return depositPlayer(player, money);
    }

    @Override
    public String format(double money) {
        
        return money + "￥";
    }

    @Override
    public int fractionalDigits() {
        
        return 2;
    }

    @SuppressWarnings("deprecation")
    @Override
    public double getBalance(String name) {
        
        return getBalance(Bukkit.getServer().getOfflinePlayer(name));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        
        return getMain().getManager().getMoney(player.getName());
    }

    @SuppressWarnings("deprecation")
    @Override
    public double getBalance(String name, String world) {
        
        return getBalance(Bukkit.getServer().getOfflinePlayer(name), world);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        
        return getBalance(player);
    }

    @Override
    public List<String> getBanks() {
        
        return null;
    }

    @Override
    public String getName() {
        
        return this.main.getName();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean has(String name, double money) {
        
        return has(Bukkit.getServer().getOfflinePlayer(name), money);
    }

    @Override
    public boolean has(OfflinePlayer player, double money) {
        
        return getBalance(player) - money >= 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean has(String name, String world, double money) {
        
        return has(Bukkit.getServer().getOfflinePlayer(name), world, money);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double money) {
        
        return has(player, money);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        
        return hasAccount(player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasAccount(String name) {
        
        return hasAccount(Bukkit.getServer().getOfflinePlayer(name));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        
        return getMain().getManager().hasPlayer(player.getName());
    }

    @Override
    public boolean hasAccount(String name, String world) {
        
        return hasAccount(name);
    }

    @Override
    public boolean hasBankSupport() {
        
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public EconomyResponse isBankMember(String name, String player) {
        
        return isBankMember(name, Bukkit.getServer().getOfflinePlayer(name));
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public EconomyResponse isBankOwner(String name, String player) {
        
        return isBankOwner(name, Bukkit.getServer().getOfflinePlayer(name));
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        
        return new EconomyResponse(0.0d, 0.0d, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public boolean isEnabled() {
        
        return getMain().isEnabled();
    }

    @SuppressWarnings("deprecation")
    @Override
    public EconomyResponse withdrawPlayer(String name, double money) {

        return withdrawPlayer(Bukkit.getServer().getOfflinePlayer(name), money);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double money) {
        
        return withdrawPlayer(name, money);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double money) {

        double oldBalance = getBalance(player);
        boolean result = getMain().getManager().takeMoney(player.getName(), money);
        return new EconomyResponse(money, oldBalance, result ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double money) {

        return withdrawPlayer(player, money);
    }
}
