package net.skret.microgames.models.customitems;

import lombok.Getter;
import net.skret.microgames.managers.CustomItemManager;
import net.skret.microgames.managers.GuiManager;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeamSelectorItem extends CustomItem {

    private final GuiManager guiManager;

    public TeamSelectorItem(CustomItemManager customItemManager, GuiManager guiManager) {
        super(customItemManager);
        this.guiManager = guiManager;
    }

    @Override
    public String getName() {
        return "Select Team";
    }

    @Override
    public Material getMaterial() {
        return Material.PAPER;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        guiManager.openTeamMenu(event.getPlayer());
    }
}
