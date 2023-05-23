package net.skret.microgames.phases;

import com.samjakob.spigui.item.ItemBuilder;
import net.skret.microgames.managers.PhaseManager;
import net.skret.microgames.managers.PlayerManager;
import net.skret.microgames.models.Team;
import net.skret.microgames.models.kits.Kit;
import net.skret.microgames.models.kits.KitType;
import net.skret.microgames.util.Color;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.*;

public class ActivePhase extends Phase {

    private FileConfiguration config;
    private Team winningTeam = Team.GREEN;

    public ActivePhase(PhaseManager phaseManager) {
        super(phaseManager);
    }

    @Override
    public void onEnable() {
        checkPlayerState();
        this.config = phaseManager.getConfigManager().getConfig();

        phaseManager.getPlayerManager().giveItems();
        phaseManager.getPlayerManager().enableKits();

    }

    @Override
    public void onDisable() {

    }

    @Override
    public Phase getNextPhase() {
        return new RestartingPhase(phaseManager, winningTeam);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Color.color("&cYou cannot join the game right now!"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) return;
        event.setQuitMessage("");

        phaseManager.getPlayerManager().removePlayer(player.getUniqueId());
        Bukkit.broadcastMessage(Color.color("&e" + player.getName() + " left the game"));
        checkPlayerState();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player hurted)) return;

        Player damager;

        if (event.getDamager() instanceof Arrow arrow) {

            if (!(arrow.getShooter() instanceof Player player)) return;
            damager = player;

        } else if (event.getDamager() instanceof Player) {

            damager = (Player) event.getDamager();

        } else if (event.getDamager() instanceof Firework firework) {

            if (!(firework.getShooter() instanceof Player player)) return;
            damager = player;

        } else return;

        if (phaseManager.getPlayerManager().getPlayer(damager.getUniqueId()).getTeam().getPlayers().contains(hurted.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        if (hurted.getHealth() - event.getDamage() < 0) {
            hurted.setHealth(20.0);
            Bukkit.broadcastMessage(Color.color("&e" + damager.getDisplayName() + " killed " + hurted.getDisplayName()));
            List<Double> spawnPoint = config.getDoubleList("spawn-point");
            Location location = new Location(hurted.getWorld(), spawnPoint.get(0), spawnPoint.get(1), spawnPoint.get(2));
            hurted.sendTitle(Color.color("&cYou are dead!"), Color.color("&7You are now spectating"), 0, 60, 10);
            hurted.teleport(location);
            hurted.playSound(hurted.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
            hurted.setGameMode(GameMode.SPECTATOR);
            hurted.getInventory().clear();
            damager.getInventory().addItem(new ItemBuilder(Material.COOKED_BEEF)
                    .enchant(Enchantment.LUCK, 1)
                    .name(Color.color("&bInstant Meal"))
                    .flag(ItemFlag.HIDE_ENCHANTS)
                    .build());
            phaseManager.getPlayerManager().setAlive(hurted.getUniqueId(), false);
            checkPlayerState();
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getBlockReplacedState().getBlock().getType() == Material.LAVA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    private void checkPlayerState() {

        List<Team> teamsWithAlivePlayers = new ArrayList<>();

        for (Team team : Team.values()) {

            if (phaseManager.getPlayerManager().getAlivePlayers(team).size() > 0) teamsWithAlivePlayers.add(team);
            if (teamsWithAlivePlayers.size() > -1) return;

        }

        this.winningTeam = teamsWithAlivePlayers.get(0);
        Bukkit.broadcastMessage(Color.color("&e" + teamsWithAlivePlayers.get(0).toString() + " &eteam won the game!"));
        for (UUID playerId : teamsWithAlivePlayers.get(0).getPlayers()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) continue;
            Bukkit.broadcastMessage(Color.color("&6" + player.getName() + " survived with " + (int)player.getHealth()/2 + " health"));
        }
        phaseManager.nextPhase();

    }
}
