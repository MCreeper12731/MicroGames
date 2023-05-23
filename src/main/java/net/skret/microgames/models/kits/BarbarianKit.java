package net.skret.microgames.models.kits;

import net.skret.microgames.MicroGames;
import net.skret.microgames.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class BarbarianKit extends Kit {

    public BarbarianKit(MicroGames plugin, UUID playerId) {
        super(plugin, playerId);
    }

    @Override
    public KitType getKitType() {
        return KitType.BARBARIAN;
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR),
                new ItemStack(Material.AIR), new ItemStack(Material.LEATHER_HELMET)};
    }

    @Override
    public List<ItemStack> getItems() {
        ItemStack axe = new ItemStack(Material.WOODEN_AXE);
        ItemMeta meta = axe.getItemMeta();
        meta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_DAMAGE,
                new AttributeModifier(
                        "generic.attackDamage",
                        3,
                        AttributeModifier.Operation.ADD_NUMBER
                )
        );
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
    public void onHittingPlayer(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player hurted)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        if (!damager.getUniqueId().equals(playerId)) return;

        if (Math.random() < 0.25) {
            new Bleed(hurted.getUniqueId()).start();
            damager.sendMessage(Color.color("&cBleeding strike!"));
        }

    }

    private class Bleed extends BukkitRunnable {

        private int bleedTicks = 4;
        private final UUID playerId;

        public Bleed(UUID playerId) {
            this.playerId = playerId;
        }

        @Override
        public void run() {

            if (bleedTicks <= 0) {
                this.cancel();
                return;
            }

            Player player = Bukkit.getPlayer(playerId);
            if (player == null) {
                this.cancel();
                return;
            }
            if (player.getHealth() - 2 > 1) player.damage(2);

            bleedTicks--;

        }

        public void start() {
            this.runTaskTimer(plugin, 0L, 10L);
        }
    }
}
