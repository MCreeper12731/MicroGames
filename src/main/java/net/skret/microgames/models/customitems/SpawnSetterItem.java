package net.skret.microgames.models.customitems;

import net.skret.microgames.managers.CustomItemManager;
import net.skret.microgames.models.Position;
import net.skret.microgames.models.Team;
import net.skret.microgames.util.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnSetterItem extends CustomItem {

    private int state = 0;

    public SpawnSetterItem(CustomItemManager customItemManager) {
        super(customItemManager);
    }

    @Override
    public String getName() {
        return "Set Spawnpoint - GREEN";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public void handleRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location spawnLocation = player.getLocation();
        Team team = Team.getTeam(state);
        if (team == null) {
            player.sendMessage(Color.color("&cInvalid Team!"));
            return;
        }
        team.addSpawn(new Position(spawnLocation));
        player.sendMessage(Color.color("&aSpawn successfully set for team " + team.name() + " !"));
    }

    @Override
    public void handleCtrlScroll(PlayerItemHeldEvent event) {
        int deltaSlot = Math.abs(event.getNewSlot() - event.getPreviousSlot()) % 7;
        if (deltaSlot != 1) return;
        event.setCancelled(true);
        state = (state + 1) % 4;
        ItemStack item = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (item == null || item.getType().equals(Material.AIR)) return;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.color(meta.getDisplayName().split("-")[0] + "- " + Team.getTeam(state).name()));
        item.setItemMeta(meta);
    }
}
