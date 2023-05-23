package net.skret.microgames.listeners;

import net.skret.microgames.MicroGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class EarthquakeTestListener implements Listener {

    private final MicroGames plugin;

    public EarthquakeTestListener(MicroGames plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getItem() == null || event.getItem().getType() != Material.IRON_HOE) return;

        Location currLocation = player.getLocation().clone().subtract(0, 1, 0);
        Vector lookingDirection = player.getEyeLocation().getDirection().setY(0).normalize();

        new TempClass(player.getUniqueId(), currLocation, lookingDirection).start();

    }

    private class TempClass extends BukkitRunnable {

        private final UUID playerId;
        private final Location currLocation;
        private final Vector lookingDirection;
        int iterations = 10;

        public TempClass(UUID playerId, Location currLocation, Vector lookingDirection) {
            this.playerId = playerId;
            this.currLocation = currLocation;
            this.lookingDirection = lookingDirection;
        }

        @Override
        public void run() {

            if (iterations <= 0) {
                this.cancel();
                return;
            }

            currLocation.add(lookingDirection);

            Collection<Entity> nearbyEntities = currLocation.getWorld().getNearbyEntities(currLocation, 2, 2, 2, null);

            nearbyEntities.forEach(entity -> {
                if (entity instanceof Player player && player.getUniqueId().equals(playerId)) return;
                if (entity instanceof FallingBlock) return;
                if (entity instanceof Damageable damageable) {
                    damageable.damage(8);
                }
                entity.setVelocity(new Vector(0, 0.8, 0));
            });

            if (currLocation.getBlock().getType() == Material.AIR) return;

            Set<Block> blocksToBeLaunched = new HashSet<>();

            blocksToBeLaunched.add(currLocation.getBlock());
            blocksToBeLaunched.add(currLocation.clone().add(0, 1, 0).getBlock());
            Vector extraDirections = lookingDirection.clone().rotateAroundAxis(new Vector(0, 1, 0), Math.PI / 2);
            blocksToBeLaunched.add(currLocation.clone().add(extraDirections).getBlock());
            blocksToBeLaunched.add(currLocation.clone().add(extraDirections).clone().add(0, 1, 0).getBlock());
            extraDirections = lookingDirection.clone().rotateAroundAxis(new Vector(0, 1, 0), -Math.PI / 2);
            blocksToBeLaunched.add(currLocation.clone().add(extraDirections).getBlock());
            blocksToBeLaunched.add(currLocation.clone().add(extraDirections).clone().add(0, 1, 0).getBlock());

            for (Block block : blocksToBeLaunched) {
                if (block.getType() == Material.AIR) continue;
                FallingBlock fallingBlock = currLocation.getWorld().spawnFallingBlock(block.getLocation().clone().add(0, 0.1, 0), Bukkit.createBlockData(block.getType()));
                fallingBlock.setVelocity(new Vector(0, 0.69, 0));
            }



            iterations--;
        }

        public void start() {
            this.runTaskTimer(plugin, 0L, 2L);
        }
    }

}
