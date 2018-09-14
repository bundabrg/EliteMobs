/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.magmaguy.elitemobs.economy;

import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.EconomySettingsConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

/**
 * Created by MagmaGuy on 17/06/2017.
 */
public class EconomyHandler {

    private static EconomyProvider economyProvider;

    // Initialize Economy
    public static void initialize() {

        // Check for Vault
        if (ConfigValues.economyConfig.getBoolean(EconomySettingsConfig.USE_VAULT)) {
            if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp != null) {
                    economyProvider = new VaultEconomyProvider(rsp.getProvider());
                    return;
                }
            }
        }

        // Use EliteMobsEconomy
        economyProvider = new EliteMobsEconomyProvider();
    }

    public static void addCurrency(OfflinePlayer user, double amount) {
        economyProvider.addCurrency(user, amount);
    }

    public static void subtractCurrency(OfflinePlayer user, double amount) {
        economyProvider.subtractCurrency(user, amount);
    }

    public static void setCurrency(OfflinePlayer user, double amount) {
        economyProvider.setCurrency(user, amount);
    }

    public static double checkCurrency(OfflinePlayer user) {
        return economyProvider.checkCurrency(user);
    }

    public static String getCurrencyName() {
        return economyProvider.getCurrencyName();
    }
}
