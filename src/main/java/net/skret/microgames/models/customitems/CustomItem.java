package net.skret.microgames.models.customitems;

import com.google.common.base.CaseFormat;
import net.skret.microgames.managers.CustomItemManager;
import net.skret.microgames.util.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItem {

    protected final CustomItemManager customItemManager;

    public CustomItem(CustomItemManager customItemManager) {
        this.customItemManager = customItemManager;
    }

    public abstract String getName();

    public abstract Material getMaterial();

    public List<String> getLore() {
        return List.of("");
    }

    public void handleLeftClick(PlayerInteractEvent event) {
        handleRightClick(event);
    }

    public void handleRightClick(PlayerInteractEvent event) {

    }

    public void handleCtrlScroll(PlayerItemHeldEvent event) {

    }

    public String getId() {
        String id = getClass().getSimpleName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, id.substring(0, id.length() - 4));
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        meta.setDisplayName(Color.color(getName()));
        List<String> lore = new ArrayList<>();
        getLore().forEach(line -> lore.add(Color.color(line)));
        meta.setLore(lore);
        container.set(customItemManager.getKey(), PersistentDataType.STRING, getId());
        item.setItemMeta(meta);
        return item;
    }

}
