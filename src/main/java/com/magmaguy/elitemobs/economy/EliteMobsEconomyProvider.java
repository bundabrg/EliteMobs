package com.magmaguy.elitemobs.economy;

import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.EconomySettingsConfig;
import com.magmaguy.elitemobs.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EliteMobsEconomyProvider implements EconomyProvider {

    @Override
    public void addCurrency(OfflinePlayer user, double amount) {
        UUID uuid = UUIDFilter.guessUUI(user.getName());

        if (!checkUserExists(uuid)) createUser(uuid);

        PlayerData.playerCurrency.put(uuid, roundDecimals(checkCurrency(user) + amount));
        PlayerData.playerCurrencyChanged = true;

    }

    @Override
    public void subtractCurrency(OfflinePlayer user, double amount) {
        UUID uuid = UUIDFilter.guessUUI(user.getName());

        if (!checkUserExists(uuid)) createUser(uuid);

        PlayerData.playerCurrency.put(uuid, roundDecimals(checkCurrency(user) - amount));
        PlayerData.playerCurrencyChanged = true;

    }

    @Override
    public void setCurrency(OfflinePlayer user, double amount) {
        UUID uuid = UUIDFilter.guessUUI(user.getName());

        if (!checkUserExists(uuid)) createUser(uuid);

        PlayerData.playerCurrency.put(uuid, roundDecimals(amount));
        PlayerData.playerCurrencyChanged = true;

    }

    @Override
    public double checkCurrency(OfflinePlayer user) {
        UUID uuid = UUIDFilter.guessUUI(user.getName());

        if (!checkUserExists(uuid)) createUser(uuid);

        return PlayerData.playerCurrency.get(uuid);

    }

    @Override
    public String getCurrencyName() {
        return ConfigValues.economyConfig.getString(EconomySettingsConfig.CURRENCY_NAME);
    }

    private void createUser(UUID uuid) {

        PlayerData.playerCurrency.put(uuid, 0.0);
        PlayerData.playerCurrencyChanged = true;

    }

    private boolean checkUserExists(UUID uuid) {

        checkUserIsCached(uuid);
        return PlayerData.playerCurrency.containsKey(uuid);

    }

    private void checkUserIsCached(UUID uuid) {

        boolean playerIsOnline = false;

        for (Player player : Bukkit.getOnlinePlayers())
            if(player.getUniqueId().equals(uuid)){
                playerIsOnline = true;
                break;
            }


        if (!PlayerData.playerDisplayName.containsKey(uuid) || playerIsOnline &&
                !PlayerData.playerDisplayName.get(uuid).equals(Bukkit.getPlayer(uuid).getDisplayName())) {

            PlayerData.playerDisplayName.put(uuid, Bukkit.getPlayer(uuid).getDisplayName());
            PlayerData.playerCacheChanged = true;

        }

    }

    private double roundDecimals(double rawValue) {

        return (double) Math.round(rawValue * 100) / 100;

    }
}
