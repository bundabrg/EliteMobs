package com.magmaguy.elitemobs.economy;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Provide an Interface for different Economies
 */
public interface EconomyProvider {
    void addCurrency(OfflinePlayer user, double amount);
    void subtractCurrency(OfflinePlayer user, double amount);
    void setCurrency(OfflinePlayer user, double amount);
    double checkCurrency(OfflinePlayer user);
    String getCurrencyName();
}
