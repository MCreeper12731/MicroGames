package net.skret.microgames.models.kits;

import com.samjakob.spigui.item.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.kits.Kit;
import net.skret.microgames.models.kits.KitType;
import net.skret.microgames.util.Color;
import net.skret.microgames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ShearsKit extends Kit {

    private boolean canEat = true;

    public ShearsKit(MicroGames plugin, UUID playerId) {
        super(plugin, playerId);
    }

    @Override
    public KitType getKitType() {
        return KitType.SHEARS;
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
        new Random();
        return List.of(
                new ItemBuilder(Material.SHEARS)
                        .name(Color.color("World's deadliest scissors"))
                        .enchant(Enchantment.DAMAGE_ALL, 5)
                        .build()
        );
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!player.getUniqueId().equals(playerId)) return;

        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        if (event.getClickedBlock() == null) return;

        Material interactedBlock = event.getClickedBlock().getType();

        if (!canEat) return;

        switch (interactedBlock) {
            case DANDELION -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 3, 1, true, true, true));
                canEat = false;
                new Cooldown().start();
            }
            case POPPY -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 1, true, true, true));
                canEat = false;
                new Cooldown().start();
            }
            case SUGAR_CANE -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1, true, true, true));
                canEat = false;
                new Cooldown().start();
            }
            case KELP -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 3, 1, true, true, true));
                canEat = false;
                new Cooldown().start();
            }
            case GRASS -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 3, 0, true, true, true));
                canEat = false;
                new Cooldown().start();
            }
            case GLASS -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 3, 1, true, true, true));
                canEat = false;
                new Cooldown().start();
            }
        }

    }

    private class Cooldown extends BukkitRunnable {

        private final int cooldownTotal = 5 * (20 / Constants.TIMER_PERIOD);
        private int cooldownRemaining = cooldownTotal;
        private Player player = Bukkit.getPlayer(playerId);

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
                canEat = true;
                return;
            }

            String actionBar = "|".repeat(Constants.TIMER_BARS);
            actionBar = "&a" + actionBar.substring(0, cooldownTotal - cooldownRemaining) + "&c" + actionBar.substring(cooldownTotal - cooldownRemaining);

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Color.color("&bEffect Cooldown: ") + Color.color(actionBar)));

            cooldownRemaining--;

        }

        public void start() {
            this.runTaskTimer(plugin, 0L, Constants.TIMER_PERIOD);
        }
    }


}
