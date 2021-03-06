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

package com.magmaguy.elitemobs.events;

import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.EventsConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class EventLauncher {

    Plugin plugin = Bukkit.getPluginManager().getPlugin(MetadataHandler.ELITE_MOBS);

    public void eventRepeatingTask() {

        if (!ConfigValues.eventsConfig.getBoolean(EventsConfig.ENABLE_EVENTS)) return;

        new BukkitRunnable() {

            int counter = 0;

            @Override
            public void run() {

                if (verifyPlayerCount()) {

                    //if enough time has passed, run the event and reset the counter
                    if (timeCheck(counter)) {

                        eventSelector();
                        counter = 0;
                        return;

                    }

                    counter++;

                }

            }

        }.runTaskTimer(plugin, 20 * 60, 20 * 60);

    }

    public static boolean verifyPlayerCount() {

        return ConfigValues.eventsConfig.getInt(EventsConfig.MINIMUM_ONLINE_PLAYERS) <= Bukkit.getOnlinePlayers().size();

    }

    private boolean timeCheck(int counter) {

        if (ConfigValues.eventsConfig.getInt(EventsConfig.MAXIMUM_ONLINE_PLAYERS) <= Bukkit.getOnlinePlayers().size()) {

            return counter >= ConfigValues.eventsConfig.getInt(EventsConfig.MAXIMUM_EVENT_FREQUENCY);

        }

        if (ConfigValues.eventsConfig.getInt(EventsConfig.MINIMUM_ONLINE_PLAYERS) == Bukkit.getOnlinePlayers().size()) {

            return counter >= ConfigValues.eventsConfig.getInt(EventsConfig.MINIMUM_EVENT_FREQUENCY);

        }

        int minPlayers = ConfigValues.eventsConfig.getInt(EventsConfig.MINIMUM_ONLINE_PLAYERS);
        int maxPlayers = ConfigValues.eventsConfig.getInt(EventsConfig.MAXIMUM_ONLINE_PLAYERS);
        double minFrequency = ConfigValues.eventsConfig.getInt(EventsConfig.MINIMUM_EVENT_FREQUENCY);
        double maxFrequency = ConfigValues.eventsConfig.getInt(EventsConfig.MAXIMUM_EVENT_FREQUENCY);

        //y=mx slope = m
        double slope = ((maxFrequency - minFrequency) / (maxPlayers - minPlayers));

        double newTimerValue = slope * Bukkit.getOnlinePlayers().size() + minFrequency;

        return counter >= newTimerValue;


    }

    private void eventSelector() {

        //todo: once more events are added, randomize which one gets picked in a weighed fashion
        HashMap<String, Double> weighedConfigValues = new HashMap<>();
        if (ConfigValues.eventsConfig.getBoolean(EventsConfig.TREASURE_GOBLIN_SMALL_ENABLED)) {

            weighedConfigValues.put(EventsConfig.SMALL_TREASURE_GOBLIN_EVENT_WEIGHT, ConfigValues.eventsConfig.getDouble(EventsConfig.SMALL_TREASURE_GOBLIN_EVENT_WEIGHT));

        }

        if (ConfigValues.eventsConfig.getBoolean(EventsConfig.DEAD_MOON_ENABLED)) {

            weighedConfigValues.put(EventsConfig.DEAD_MOON_EVENT_WEIGHT, ConfigValues.eventsConfig.getDouble(EventsConfig.DEAD_MOON_EVENT_WEIGHT));

        }

        switch (pickWeighedEvent(weighedConfigValues)) {

            case EventsConfig.SMALL_TREASURE_GOBLIN_EVENT_WEIGHT:
                SmallTreasureGoblin.initializeEvent();
                return;
            case EventsConfig.DEAD_MOON_EVENT_WEIGHT:
                DeadMoon.initializeEvent();
                return;

        }

    }

    private String pickWeighedEvent(HashMap<String, Double> weighedConfigValues) {

        double totalWeight = 0;

        for (String string : weighedConfigValues.keySet()) {

            totalWeight += weighedConfigValues.get(string);

        }

        String selectedLootSystem = null;
        double random = Math.random() * totalWeight;

        for (String string : weighedConfigValues.keySet()) {

            random -= weighedConfigValues.get(string);


            if (random <= 0) {

                selectedLootSystem = string;

                break;

            }

        }

        return selectedLootSystem;

    }


}
