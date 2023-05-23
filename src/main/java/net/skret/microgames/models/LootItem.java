package net.skret.microgames.models;

import net.skret.microgames.util.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LootItem {

    private final Material material;
    private final String name;
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final double chance;

    private final int minAmount;
    private final int maxAmount;

    public LootItem(ConfigurationSection section) {

        Material material;

        try {
            material = Material.valueOf(section.getString("material"));
        } catch (IllegalArgumentException ignored) {
            material = Material.AIR;
        }

        this.material = material;
        this.name = section.getString("name");

        ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");

        if (enchantmentSection != null) {
            for (String enchantmentKey : enchantmentSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(
                        NamespacedKey.minecraft(
                                enchantmentKey.toLowerCase()
                        )
                );

                if (enchantment != null) {
                    int level = enchantmentSection.getInt(enchantmentKey);
                    this.enchantments.put(enchantment, level);
                }

            }
        }

        this.chance = section.getDouble("chance");
        this.minAmount = section.getInt("minAmount");
        this.maxAmount = section.getInt("maxAmount");

    }

    public boolean shouldFill(Random random) {
        return random.nextDouble() < chance;
    }

    public ItemStack make(ThreadLocalRandom random) {
        int amount = random.nextInt(minAmount, maxAmount + 1);
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(
                    Color.color(name)
            );
        }

        if (!enchantments.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> enchantmentEntry : enchantments.entrySet()) {
                meta.addEnchant(
                        enchantmentEntry.getKey(),
                        enchantmentEntry.getValue(),
                        true
                );
            }
        }

        item.setItemMeta(meta);

        return item;
    }
}
