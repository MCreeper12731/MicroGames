package net.skret.microgames.listeners;

import net.skret.microgames.managers.CustomItemManager;
import net.skret.microgames.models.customitems.CustomItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CustomItemUsageListener implements Listener {

    private final CustomItemManager customItemManager;

    public CustomItemUsageListener(CustomItemManager customItemManager) {
        this.customItemManager = customItemManager;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        ItemStack heldItem = event.getItem();
        if (heldItem == null) return;
        if (!isCustomItem(heldItem)) return;

        CustomItem item = customItemManager.getItem(getId(heldItem));

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            item.handleLeftClick(event);
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            item.handleRightClick(event);
        }
    }

    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (heldItem == null) return;
        if (!isCustomItem(heldItem)) return;

        CustomItem item = customItemManager.getItem(getId(heldItem));

        item.handleCtrlScroll(event);
    }

    private boolean isCustomItem(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(customItemManager.getKey(), PersistentDataType.STRING);
    }

    private String getId(ItemStack item) {
        return item.getItemMeta().getPersistentDataContainer().get(customItemManager.getKey(), PersistentDataType.STRING);
    }

}
