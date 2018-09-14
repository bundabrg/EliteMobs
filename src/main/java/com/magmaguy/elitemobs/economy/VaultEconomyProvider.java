package com.magmaguy.elitemobs.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

public class VaultEconomyProvider implements EconomyProvider {

    private Economy economy;

    VaultEconomyProvider(Economy economy) {
        this.economy = economy;
    }

    @Override
    public void addCurrency(OfflinePlayer user, double amount) {
        economy.depositPlayer(user, amount);
    }

    @Override
    public void subtractCurrency(OfflinePlayer user, double amount) {
        economy.withdrawPlayer(user, amount);
    }

    @Override
    public void setCurrency(OfflinePlayer user, double amount) {
        addCurrency(user, amount - economy.getBalance(user));
    }

    @Override
    public double checkCurrency(OfflinePlayer user) {
        return economy.getBalance(user);
    }

    @Override
    public String getCurrencyName() {
        return economy.currencyNamePlural();
    }
}
