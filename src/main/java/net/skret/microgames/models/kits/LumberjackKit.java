package net.skret.microgames.models.kits;

import com.samjakob.spigui.item.ItemBuilder;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.kits.Kit;
import net.skret.microgames.models.kits.KitType;
import net.skret.microgames.util.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LumberjackKit extends Kit {

    public LumberjackKit(MicroGames plugin, UUID playerId) {
        super(plugin, playerId);
    }

    @Override
    public KitType getKitType() {
        return KitType.LUMBERJACK;
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[]{
                new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_HELMET)
        };
    }

    @Override
    public List<ItemStack> getItems() {

        ItemStack axe = new ItemStack(Material.WOODEN_AXE);
        ItemMeta meta = axe.getItemMeta();
        meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(
                        "generic.attackDamage",
                        -2,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
        meta.setDisplayName(Color.color("&bHarvesting Axe"));
        meta.setLore(new ArrayList<>(List.of(Color.color("&7Cannot be used to pvp!"))));
        axe.setItemMeta(meta);

        return List.of(
                axe
        );
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!player.getUniqueId().equals(playerId)) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        switch (item.getType()) {
            case WOODEN_AXE -> {
                if (event.getAction() != Action.LEFT_CLICK_BLOCK) break;
                if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.SPRUCE_LOG) break;
                if (Math.random() < 0.05) {
                    player.getInventory().addItem(new ItemStack(Material.PAPER));
                    break;
                }
                if (Math.random() < 0.10) {
                    player.getInventory().addItem(new ItemStack(Material.STRING));
                    break;
                }
                if (Math.random() < 0.20) {
                    player.getInventory().addItem(new ItemStack(Material.STICK));
                }
            }
            case STICK -> {
                if (item.getAmount() < 2) {
                    player.sendMessage(Color.color("&cYou need 2 sticks to craft arrows!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted arrows!"));
                item.setAmount(item.getAmount() - 2);
                player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            }
            case STRING -> {
                if (item.getAmount() < 2) {
                    player.sendMessage(Color.color("&cYou need 2 strings to craft a crossbow!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted crossbow!"));
                item.setAmount(item.getAmount() - 2);
                player.getInventory().addItem(new ItemBuilder(Material.CROSSBOW)
                        .enchant(Enchantment.QUICK_CHARGE, 5)
                        .durability((short)(465 - 3))
                        .build());
            }
            case PAPER -> {
                if (item.getAmount() < 2) {
                    player.sendMessage(Color.color("&cYou need 2 paper to craft rockets!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted crossbow!"));
                item.setAmount(item.getAmount() - 2);
                ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET);
                FireworkMeta meta = (FireworkMeta) rocket.getItemMeta();
                meta.addEffect(FireworkEffect.builder().withColor(org.bukkit.Color.ORANGE).build());
                rocket.setItemMeta(meta);
                player.getInventory().addItem(rocket);
            }
        }
    }
}
