package net.skret.microgames.models.kits;

import com.samjakob.spigui.item.ItemBuilder;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.kits.Kit;
import net.skret.microgames.models.kits.KitType;
import net.skret.microgames.util.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class DiggerKit extends Kit {

    public DiggerKit(MicroGames plugin, UUID playerId) {
        super(plugin, playerId);
    }

    @Override
    public KitType getKitType() {
        return KitType.DIGGER;
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
        ItemStack shovel = new ItemStack(Material.WOODEN_SHOVEL);
        ItemMeta meta = shovel.getItemMeta();
        meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(
                        "generic.attackDamage",
                        -2,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
        shovel.setItemMeta(meta);
        return List.of(
                shovel
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
            case AIR -> {
                if (event.getAction() != Action.LEFT_CLICK_BLOCK) break;
                if (Math.random() > 0.5) {
                    player.getInventory().addItem(new ItemStack(Material.STICK));
                }
            }
            case WOODEN_SHOVEL -> {
                if (event.getAction() != Action.LEFT_CLICK_BLOCK) break;
                Damageable damageable = (Damageable) item.getItemMeta();
                damageable.setDamage(damageable.getDamage() + 1);
                if (damageable.getDamage() > 59) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 5, 1);
                } else item.setItemMeta(damageable);

                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 5, false, false, false));
                if (Math.random() < 0.03) {
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND));
                    break;
                }
                if (Math.random() < 0.05) {
                    player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
                    break;
                }
                if (Math.random() < 0.10) {
                    player.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
                }
            }
            case STICK -> {
                if (item.getAmount() < 14) {
                    player.sendMessage(Color.color("&cYou need 14 sticks to craft a shovel!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted shovel!"));
                item.setAmount(item.getAmount() - 12);
                player.getInventory().addItem(new ItemStack(Material.WOODEN_SHOVEL));
            }
            case IRON_INGOT -> {
                if (item.getAmount() < 5) {
                    player.sendMessage(Color.color("&cYou need 5 iron ingots to craft a sword!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted iron sword!"));
                item.setAmount(item.getAmount() - 5);
                player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD)
                        .durability((short)(250 - 3))
                        .build());
            }
            case GOLD_INGOT -> {
                if (item.getAmount() < 5) {
                    player.sendMessage(Color.color("&cYou need 5 gold ingots to craft a golden apple!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted golden apple!"));
                item.setAmount(item.getAmount() - 5);
                player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            }
            case DIAMOND -> {
                if (item.getAmount() < 5) {
                    player.sendMessage(Color.color("&cYou need 5 diamonds to craft a sword!"));
                    break;
                }
                player.sendMessage(Color.color("&aCrafted diamond sword!"));
                item.setAmount(item.getAmount() - 5);
                player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD)
                        .durability((short)(1561 - 1))
                        .build());
            }
        }
    }
}
