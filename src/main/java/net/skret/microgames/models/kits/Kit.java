package net.skret.microgames.models.kits;

import com.google.common.collect.ImmutableList;
import net.skret.microgames.MicroGames;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class Kit implements Listener {

    protected final MicroGames plugin;
    protected final UUID playerId;

    public Kit(MicroGames plugin, UUID playerId) {
        this.plugin = plugin;
        this.playerId = playerId;
    }

    public abstract KitType getKitType();

    public abstract ItemStack[] getArmor();

    public abstract List<ItemStack> getItems();

    public abstract void onStart();

    public abstract void onStop();

    public final void enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        onStart();
    }

    public final void disable() {
        HandlerList.unregisterAll(this);
        onStop();
    }
}
