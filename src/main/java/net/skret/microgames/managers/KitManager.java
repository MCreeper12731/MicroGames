package net.skret.microgames.managers;

import net.skret.microgames.MicroGames;
import net.skret.microgames.models.PlayerWrapper;
import net.skret.microgames.models.kits.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private final MicroGames plugin;
    private final PlayerManager playerManager;

    public KitManager(MicroGames plugin, PlayerManager playerManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
    }

    public Kit getKit(UUID playerId, KitType kitType) {
        return switch(kitType) {
            case KNIGHT -> new KnightKit(plugin, playerId);
            case SHEARS -> new ShearsKit(plugin, playerId);
            case DIGGER -> new DiggerKit(plugin, playerId);
            case LUMBERJACK -> new LumberjackKit(plugin, playerId);
            case BARBARIAN -> new BarbarianKit(plugin, playerId);
            case NINJA -> new NinjaKit(plugin, playerId);
        };
    }

    public void giveItems() {

        for (PlayerWrapper playerWrapper : playerManager.getPlayers()) {

            Player player = Bukkit.getPlayer(playerWrapper.getId());
            player.getInventory().addItem(playerWrapper.getSelectedKit().getItems().toArray(new ItemStack[0]));

            ItemStack[] armor = playerWrapper.getSelectedKit().getArmor();
            LeatherArmorMeta meta = (LeatherArmorMeta) armor[3].getItemMeta();
            int[] color = playerWrapper.getTeam().getRgbValue();
            meta.setColor(Color.fromRGB(color[0], color[1], color[2]));
            armor[3].setItemMeta(meta);

            player.getInventory().setArmorContents(armor);
        }

    }

    public void enableListeners() {
        for (PlayerWrapper playerWrapper : playerManager.getPlayers()) {
            playerWrapper.getSelectedKit().enable();
        }
    }

    public void disableListeners() {
        for (PlayerWrapper playerWrapper : playerManager.getPlayers()) {
            playerWrapper.getSelectedKit().disable();
        }
    }
}
