package io.github.gum4.notmuchtrades;

import io.github.gum4.notmuchtrades.handlers.TradeHandler;
import io.github.gum4.notmuchtrades.listeners.TradeListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NotMuchTrades extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new TradeListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
