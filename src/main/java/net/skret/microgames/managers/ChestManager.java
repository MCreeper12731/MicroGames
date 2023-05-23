package net.skret.microgames.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.ChestItems;
import net.skret.microgames.models.LootItem;
import net.skret.microgames.models.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ChestManager implements Listener {

    private final Set<Location> openedChests = new HashSet<>();
    private final List<LootItem> lootItems = new ArrayList<>();

    public ChestManager(MicroGames plugin, ConfigManager configManager) {
        ConfigurationSection itemsSection = configManager.getConfig().getConfigurationSection("lootItems");
        if (itemsSection == null) {
            Bukkit.getLogger().severe("Please setup your 'lootItems' in config.yml");
            return;
        }
        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection section = itemsSection.getConfigurationSection(key);
            lootItems.add(new LootItem(section));
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void fill(Inventory inventory) {
        inventory.clear();

        ThreadLocalRandom random = ThreadLocalRandom.current();
        Set<LootItem> used = new HashSet<>();

        for (int slotIndex = 0; slotIndex < inventory.getSize(); slotIndex++) {
            LootItem randomItem = lootItems.get(
                    random.nextInt(lootItems.size())
            );
            if (used.contains(randomItem)) continue;
            used.add(randomItem);

            if (randomItem.shouldFill(random)) {
                inventory.setItem(slotIndex, randomItem.make(random));
            }
        }
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {

        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Chest chest) {

            if (hasBeenOpened(chest.getLocation())) return;

            markAsOpened(chest.getLocation());
            fill(chest.getBlockInventory());
            return;
        }

        if (holder instanceof DoubleChest chest) {

            if (hasBeenOpened(chest.getLocation())) return;

            markAsOpened(chest.getLocation());
            fill(chest.getInventory());

        }

    }

    public void markAsOpened(Location location) {
        openedChests.add(location);
    }

    public boolean hasBeenOpened(Location location) {
        return openedChests.contains(location);
    }

    public void resetChests() {
        openedChests.clear();
    }

}
