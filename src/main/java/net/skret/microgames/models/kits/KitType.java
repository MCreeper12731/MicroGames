package net.skret.microgames.models.kits;

import lombok.Getter;
import net.skret.microgames.util.Color;
import org.bukkit.Material;

import java.util.*;

@Getter
public enum KitType {

    KNIGHT("Knight", Material.WOODEN_SWORD, List.of(
            Color.color("&7Perks:"),
            Color.color("&7 - 50% chance to dodge attacks (5 second cooldown)")
    )),
    SHEARS("5 Year Old", Material.SHEARS, List.of(
            Color.color("&7Perks:"),
            Color.color("&7 - Attacking plants gives potion effects"),
            Color.color("&7   for 2 seconds (6 second cooldown)")
    )),
    DIGGER("Digger", Material.WOODEN_SHOVEL, List.of(
            Color.color("&7Perks:"),
            Color.color("&7 - Left clicking with shovel has a chance"),
            Color.color("&7   to yield resources. Get 14 sticks to craft"),
            Color.color("&7   a new shovel. Right click with sufficient"),
            Color.color("&7   amount of resources to craft.")
    )),
    LUMBERJACK("Lumberjack", Material.WOODEN_AXE, List.of(
            Color.color("&7Perks:"),
            Color.color("&7 - Left clicking logs with axe has a chance"),
            Color.color("&7   to yield resources. Right click with sufficient"),
            Color.color("&7   amount of resources to craft.")
    )),
    BARBARIAN("Barbarian", Material.BLAZE_POWDER, List.of(
            Color.color("&7Perks:"),
            Color.color("&7 - Players that you attack have a 25% chance to"),
            Color.color("&7   start bleeding for 4 hearts over 2 seconds.")
    )),
    NINJA("Ninja", Material.ENDER_EYE, List.of(
            Color.color("&7Perks:"),
            Color.color("&7 - Become invisible when crouching"),
            Color.color("&7 - Heal for 25% all melee damage dealt")
    ));

    private final String name;
    private final Material displayMaterial;
    private final List<String> lore;

    KitType(String name, Material displayMaterial, List<String> lore) {
        this.name = name;
        this.displayMaterial = displayMaterial;
        this.lore = new ArrayList<>(lore);
    }
}
