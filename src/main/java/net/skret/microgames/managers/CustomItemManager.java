package net.skret.microgames.managers;

import lombok.Getter;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.customitems.*;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomItemManager {

    @Getter
    private final NamespacedKey key;
    private final Map<String, CustomItem> items;

    public CustomItemManager(MicroGames plugin, GuiManager guiManager, ChestManager chestManager) {
        this.key = new NamespacedKey(plugin, "microgames_items");
        items = new HashMap<>();
        registerItems(
            new SpawnSetterItem(this),
                new KitSelectorItem(this, guiManager),
                new TeamSelectorItem(this, guiManager),
                new ChestSelectorItem(this, guiManager)
        );
    }

    private void registerItems(CustomItem... items) {
        Arrays.asList(items).forEach(item -> this.items.put(item.getId(), item));
    }

    public CustomItem getItem(String id) {
        return items.get(id);
    }

}
