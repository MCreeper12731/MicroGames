package net.skret.microgames.phases;

import net.skret.microgames.managers.PhaseManager;
import net.skret.microgames.models.Team;
import net.skret.microgames.models.kits.KitType;
import net.skret.microgames.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class RestartingPhase extends Phase {

    private final Team winningTeam;

    public RestartingPhase(PhaseManager phaseManager, Team winningTeam) {
        super(phaseManager);
        this.winningTeam = winningTeam;
    }

    @Override
    public void onEnable() {

        phaseManager.getPlayerManager().disableKits();

        new Countdown().start();

    }

    @Override
    public void onDisable() {

    }

    @Override
    public Phase getNextPhase() {
        return new WaitingPhase(phaseManager);
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
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }

    private class Countdown extends BukkitRunnable {

        private int timeLeft = 12;

        @Override
        public void run() {

            if (timeLeft == 0) {
                this.cancel();
                phaseManager.nextPhase();
                return;
            }

            if (timeLeft == 10) {
                Bukkit.broadcastMessage(Color.color("&eArena restarting in 10 seconds"));
            }

            if (timeLeft > 3)
                for (UUID playerId : winningTeam.getPlayers()) {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player == null) return;
                    player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                }

            timeLeft--;

        }

        public void start() {
            this.runTaskTimer(phaseManager.getPlugin(), 0L, 20L);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getBlockReplacedState().getBlock().getType() == Material.LAVA) {
            event.setCancelled(true);
        }
    }

}
