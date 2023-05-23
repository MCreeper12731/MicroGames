package net.skret.microgames.models;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum ChestItems {

    DEFENSIVE("Defensive", Material.SHIELD),
    OFFENSIVE("Offensive", Material.IRON_SWORD);

    private final String name;
    private final Material material;
    private final Set<ItemStack> items = new HashSet<>();

    ChestItems(String name, Material material, ItemStack... items) {
        this.name = name;
        this.material = material;
    }
}
