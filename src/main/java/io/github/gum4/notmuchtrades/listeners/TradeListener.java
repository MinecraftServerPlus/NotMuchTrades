package io.github.gum4.notmuchtrades.listeners;

import io.github.gum4.notmuchtrades.NotMuchTrades;
import io.github.gum4.notmuchtrades.handlers.TradeHandler;
import io.papermc.paper.event.player.PlayerTradeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public class TradeListener implements Listener {
    private static JavaPlugin plugin;
    private static NamespacedKey tradeCountKey = null;
    private static TradeHandler tradeHandler;

    public TradeListener(JavaPlugin plugin) {
        TradeListener.plugin = plugin;
        tradeHandler = new TradeHandler(plugin);
        tradeCountKey = new NamespacedKey(plugin, "trade-count");
    }

    @EventHandler
    public void onTrade(PlayerTradeEvent event) {
        AbstractVillager trader = event.getVillager();
        long tradeCount = trader.getPersistentDataContainer().getOrDefault(tradeCountKey, PersistentDataType.LONG, 0L);
        long maxTradeCount;
        boolean isWanderingTrader = trader instanceof WanderingTrader;
        if (isWanderingTrader) {
            maxTradeCount = TradeHandler.wanderingTraderMaxTradeCount;
        }
        else {
            maxTradeCount = TradeHandler.maxTradeCountMap.get(((Villager) trader).getProfession());
        }

        boolean isTradingAvailable = (tradeCount < maxTradeCount) || (maxTradeCount == -1);
        if (!isTradingAvailable) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage(
                    Component.text("더 이상 거래에 흥미가 없어 보인다.")
                            .color(NamedTextColor.RED)
            );
            player.closeInventory();
        }
        else {
            trader.getPersistentDataContainer().set(tradeCountKey, PersistentDataType.LONG, tradeCount + 1);
            if (tradeCount == maxTradeCount - 1) {
                Player player = event.getPlayer();
                player.sendMessage(
                        Component.text("더 이상 거래에 흥미가 없어 보인다.")
                                .color(NamedTextColor.RED)
                );
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEntityEvent event) {
        boolean isVillager = (event.getRightClicked().getType() == EntityType.VILLAGER) && ((Villager) event.getRightClicked()).getProfession() != Villager.Profession.NONE && ((Villager) event.getRightClicked()).getProfession() != Villager.Profession.NITWIT;
        boolean isWanderingTrader = event.getRightClicked().getType() == EntityType.WANDERING_TRADER;
        long maxTradeCount;
        if (isWanderingTrader) {
            maxTradeCount = TradeHandler.wanderingTraderMaxTradeCount;
        }
        else if (isVillager) {
            maxTradeCount = TradeHandler.maxTradeCountMap.get(((Villager) event.getRightClicked()).getProfession());
        }
        else {
            return;
        }
        AbstractVillager villager = (AbstractVillager) event.getRightClicked();
        long tradeCount = villager.getPersistentDataContainer().getOrDefault(tradeCountKey, PersistentDataType.LONG, 0L);

        if (tradeCount >= maxTradeCount && maxTradeCount != -1) {
            event.setCancelled(true);
            int fun = new Random().nextInt(0, 100);
            if (fun < 1) {
                event.getPlayer().sendMessage(
                        event.getPlayer().teamDisplayName()
                                .append(Component.text("는 거래를 요청했다!"))
                                .appendNewline()
                                .append(Component.text("그러나 아무 일도 일어나지 않았다!").color(NamedTextColor.RED))
                );
                return;
            }
            event.getPlayer().sendMessage(
                    Component.text("별로 거래를 하고 싶지 않아 보인다.")
                            .color(NamedTextColor.RED)
            );
        }
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        if (event.getEntityType() == EntityType.VILLAGER) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.VILLAGER) {
            ((Villager) event.getEntity()).setBreed(false);
        }
    }
}
