package net.skret.microgames.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerRightClickListener implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        if (item == null || block == null) return;
        if (item.getType() == Material.WOODEN_SHOVEL) {
            event.setCancelled(true);
            return;
        }
        if (item.getType() == Material.WOODEN_AXE) {
            event.setCancelled(true);
            return;
        }
        if (item.getType() == Material.STRING) {
            event.setCancelled(true);
            return;
        }
    }

}
