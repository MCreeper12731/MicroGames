package net.skret.microgames.models.kits;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skret.microgames.MicroGames;
import net.skret.microgames.util.Color;
import net.skret.microgames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class KnightKit extends Kit {

    private static final double DODGE_CHANCE = 0.9999999999999999;

    private boolean canDodge = true;
    private Cooldown cooldown;

    public KnightKit(MicroGames plugin, UUID playerId) {
        super(plugin, playerId);
    }

    @Override
    public KitType getKitType() {
        return KitType.KNIGHT;
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
        return List.of(
                new ItemStack(Material.WOODEN_SWORD)
        );
    }

    @Override
    public void onStart() {
        Player player = Bukkit.getPlayer(playerId);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 0, false, false, false));
    }

    @Override
    public void onStop() {
        try {
            this.cooldown.cancel();
        } catch (IllegalStateException ignored) {}
    }

    @EventHandler
    public void onGettingHit(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player damageTaker)) return;

        if (!damageTaker.getUniqueId().equals(playerId)) return;

        if (!canDodge) return;

        if (!(Math.random() < DODGE_CHANCE)) return;

        event.setDamage(0);
        damageTaker.getWorld().playSound(damageTaker.getLocation(), Sound.ITEM_SHIELD_BLOCK, 5, 1);
        canDodge = false;
        this.cooldown = new Cooldown();
        this.cooldown.start();
        damageTaker.sendMessage(Color.color("&aAttack blocked!"));

    }

    private class Cooldown extends BukkitRunnable {

        private final int cooldownTotal = 5 * (20 / Constants.TIMER_PERIOD);
        private int cooldownRemaining = cooldownTotal;
        private final Player player = Bukkit.getPlayer(playerId);

        @Override
        public void run() {

            if (player == null) {
                this.cancel();
                return;
            }

            if (cooldownRemaining <= 0) {
                this.cancel();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        Color.color("&aBlock recharged!")
                ));
                canDodge = true;
                return;
            }

            String actionBar = "|".repeat(Constants.TIMER_BARS);
            actionBar =
                    "&a" +
                    actionBar.substring(0, cooldownTotal - cooldownRemaining) +
                    "&c" +
                    actionBar.substring(cooldownTotal - cooldownRemaining );

            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Color.color("&bEffect Cooldown: ") + Color.color(actionBar))
            );

            cooldownRemaining--;

        }

        public void start() {
            this.runTaskTimer(plugin, 0L, Constants.TIMER_PERIOD);
        }
    }
}
