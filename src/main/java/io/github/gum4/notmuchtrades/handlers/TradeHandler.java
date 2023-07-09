package io.github.gum4.notmuchtrades.handlers;

import io.github.gum4.notmuchtrades.listeners.TradeListener;
import io.github.gum4.notmuchtrades.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class TradeHandler {
    private static File file;
    private static YamlConfiguration config;
    public static long defaultMaxTradeCount;
    public static long wanderingTraderMaxTradeCount;
    public static HashMap<Villager.Profession, Long> maxTradeCountMap = new HashMap<>();

    public TradeHandler(JavaPlugin plugin) {
        file = ConfigUtil.getConfigFile(plugin);
        config = YamlConfiguration.loadConfiguration(file);
        loadMaxTradeCount();
    }

    public static void loadMaxTradeCount() {
        defaultMaxTradeCount = config.getLong("DEFAULT", 20);
        for (String profession: config.getKeys(false)) {
            if (profession.equals("WANDERING_TRADER")) {
                wanderingTraderMaxTradeCount = config.getLong(profession, defaultMaxTradeCount);
            }

            else if (!profession.equals("DEFAULT")) {
                Bukkit.getConsoleSender().sendMessage( profession + " : " + config.getLong(profession, defaultMaxTradeCount));
                maxTradeCountMap.put(Villager.Profession.valueOf(profession), config.getLong(profession, defaultMaxTradeCount));
            }
        }
    }
}
