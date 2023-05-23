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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class NinjaKit extends Kit {

    private boolean canGhost = true;
    private Timer timer;
    private Cooldown cooldown;

    public NinjaKit(MicroGames plugin, UUID playerId) {
        super(plugin, playerId);
    }

    @Override
    public KitType getKitType() {
        return KitType.NINJA;
    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR),
                new ItemStack(Material.AIR), new ItemStack(Material.LEATHER_HELMET)};
    }

    @Override
    public List<ItemStack> getItems() {
        return List.of(
                new ItemStack(Material.WOODEN_SWORD)
        );
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        try {
            timer.cancel();
        } catch (IllegalStateException ignored) {}
    }

    @EventHandler
    public void onHittingPlayer(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player damager)) return;

        if (!damager.getUniqueId().equals(playerId)) return;

        damager.setHealth(Math.min(20, damager.getHealth() + event.getDamage() * 0.25));

    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {

        Player player = event.getPlayer();

        if (!player.getUniqueId().equals(playerId)) return;

        if (player.isSneaking()) return;

        if (!canGhost) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10, 0, false, false, false));

        try {
            if (timer != null && !timer.isCancelled()) return;
        } catch (IllegalStateException ignored) {}

        this.timer = new Timer(player);
        this.timer.start();

    }

    private class Timer extends BukkitRunnable {

        private final int maxTimeSpentGhosted = 5 * (20 / Constants.TIMER_PERIOD);
        private int timeSpentGhosted = 0;
        private final Player player;

        public Timer(Player player) {
            this.player = player;
        }

        @Override
        public void run() {

            if (player == null) {
                this.cancel();
                return;
            }

            if (timeSpentGhosted >= maxTimeSpentGhosted || !player.isSneaking()) {
                this.cancel();
                canGhost = false;
                cooldown = new Cooldown();
                cooldown.start();
                return;
            }

            if (player.isSneaking()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15, 0, false, false, false));
                timeSpentGhosted++;
            }

            String actionBar = "|".repeat(Constants.TIMER_BARS);
            actionBar =
                    "&b" +
                    actionBar.substring(0, timeSpentGhosted) +
                    "&1" +
                    actionBar.substring(timeSpentGhosted);

            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Color.color("&bTime Ghosted: ") + Color.color(actionBar))
            );

        }

        public void start() {
            this.runTaskTimer(plugin, 0L, Constants.TIMER_PERIOD);
        }
    }

    private class Cooldown extends BukkitRunnable {

        private final int cooldownTotal = 4 * (20 / Constants.TIMER_PERIOD);
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
                        Color.color("&aGhosting recharged!")
                ));
                canGhost = true;
                return;
            }

            int percentageCooldown = (int)Math.round(Constants.TIMER_BARS - ((double)cooldownRemaining / cooldownTotal * Constants.TIMER_BARS));

            String actionBar = "|".repeat(Constants.TIMER_BARS);
            actionBar =
                    "&a" +
                    actionBar.substring(0, percentageCooldown) +
                    "&c" +
                    actionBar.substring(percentageCooldown);

            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Color.color("&bGhosting Cooldown: ") + Color.color(actionBar))
            );

            cooldownRemaining--;

        }

        public void start() {
            this.runTaskTimer(plugin, 0L, Constants.TIMER_PERIOD);
        }
    }
}
