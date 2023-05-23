package net.skret.microgames.models.customitems;

import net.skret.microgames.managers.CustomItemManager;
import net.skret.microgames.managers.GuiManager;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitSelectorItem extends CustomItem {

    private final GuiManager guiManager;

    public KitSelectorItem(CustomItemManager customItemManager, GuiManager guiManager) {
        super(customItemManager);
        this.guiManager = guiManager;
    }

    @Override
    public String getName() {
        return "Select Kit";
    }

    @Override
    public Material getMaterial() {
        return Material.NETHER_STAR;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        guiManager.openKitMenu(event.getPlayer());
    }
}
