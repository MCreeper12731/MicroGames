package net.skret.microgames.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemListener implements Listener {

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) event.setCancelled(true);
    }

}
