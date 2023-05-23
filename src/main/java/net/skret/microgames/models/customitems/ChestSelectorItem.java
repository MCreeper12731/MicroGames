package net.skret.microgames.models.customitems;

import net.skret.microgames.managers.CustomItemManager;
import net.skret.microgames.managers.GuiManager;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestSelectorItem extends CustomItem {

    private final GuiManager guiManager;

    public ChestSelectorItem(CustomItemManager customItemManager, GuiManager guiManager) {
        super(customItemManager);
        this.guiManager = guiManager;
    }

    @Override
    public String getName() {
        return "Select Chest Options";
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_POWDER;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        guiManager.openChestItemsMenu(event.getPlayer());
    }
}
