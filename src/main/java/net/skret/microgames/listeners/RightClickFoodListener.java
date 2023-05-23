package net.skret.microgames.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClickFoodListener implements Listener {

    @EventHandler
    public void onRightClickFood(PlayerInteractEvent event) {

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.COOKED_BEEF) return;

        Player player = event.getPlayer();

        if (player.getHealth() == 20.0) return;

        item.setAmount(item.getAmount() - 1);

        player.setHealth(Math.min(20.0, player.getHealth() + 8));
    }

}
