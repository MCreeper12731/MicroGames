package net.skret.microgames.managers;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.ChestItems;
import net.skret.microgames.models.Team;
import net.skret.microgames.models.kits.KitType;
import net.skret.microgames.util.Color;
import net.skret.microgames.util.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiManager {

    private final MicroGames plugin;
    private final SpiGUI spiGUI;
    private final PlayerManager playerManager;
    private final ChestVotingManager chestVotingManager;

    public GuiManager(
            MicroGames plugin,
            SpiGUI spiGUI,
            PlayerManager playerManager,
            ChestVotingManager chestVotingManager) {
        this.plugin = plugin;
        this.spiGUI = spiGUI;
        this.playerManager = playerManager;
        this.chestVotingManager = chestVotingManager;
    }

    public void openKitMenu(Player player) {

        SGMenu kitMenu = spiGUI.create("Kit Selection Menu", 5);

        for (KitType kit : KitType.values()) {

            SGButton kitButton = new SGButton(
                    new ItemBuilder(kit.getDisplayMaterial())
                            .name(Color.color("&b" + kit.getName()))
                            .lore(kit.getLore())
                            .build()
            ).withListener((InventoryClickEvent event) -> {
                Player whoClicked = (Player) event.getWhoClicked();
                if (playerManager.getKit(whoClicked.getUniqueId()).getKitType() == kit) {
                    whoClicked.sendMessage(Color.color("&cYou already have kit &b" + kit.getName() + " &cselected!"));
                    InventoryUtil.closeInventory(player, plugin);
                    return;
                }
                playerManager.setKit(whoClicked.getUniqueId(), kit);
                whoClicked.sendMessage(Color.color("&eSelected kit &b" + kit.getName()));
                InventoryUtil.closeInventory(player, plugin);
            });
            kitMenu.addButton(kitButton);
        }

        player.openInventory(kitMenu.getInventory());

    }

    public void openTeamMenu(Player player) {

        SGMenu teamMenu = spiGUI.create("Team Selection Menu", 5);

        for (Team team : Team.values()) {
            SGButton teamButton = new SGButton(
                    new ItemBuilder(Material.valueOf(team.name() + "_WOOL"))
                            .lore(Color.color("&7Players: " + team.getPlayers().size()))
                            .build()
            ).withListener((InventoryClickEvent event) -> {
                Player whoClicked = (Player) event.getWhoClicked();
                if (!isSelectionValid(team)) {
                    whoClicked.sendMessage(Color.color("&cThis team has too many players! Fill others first"));
                    return;
                }
                if (playerManager.getTeam(whoClicked.getUniqueId()) == team) {
                    whoClicked.sendMessage(Color.color("&cYou are already on team " + team));
                }
                playerManager.setTeam(player.getUniqueId(), team);
                whoClicked.sendMessage(Color.color("&eJoined " + team + " &eteam!"));
                InventoryUtil.closeInventory(player, plugin);
            });

            teamMenu.addButton(teamButton);
        }

        player.openInventory(teamMenu.getInventory());
    }

    private boolean isSelectionValid(Team teamAttemptingToJoin) {

        for (Team team : Team.values()) {
            if (team.getPlayers().size() + 1 < teamAttemptingToJoin.getPlayers().size()) return false;
        }
        return true;
    }

    public void openChestItemsMenu(Player player) {

        SGMenu chestItemsMenu = spiGUI.create("Vote for Chest Items", 5);

        for (ChestItems chestItems : ChestItems.values()) {
            SGButton teamButton = new SGButton(
                    new ItemBuilder(Material.valueOf(chestItems.name() + "_WOOL"))
                            .build()
            ).withListener((InventoryClickEvent event) -> {
                Player whoClicked = (Player) event.getWhoClicked();
                chestVotingManager.setEntry(whoClicked.getUniqueId(), chestItems);
                whoClicked.sendMessage("Voted for &b" + chestItems.getName() + " Items!");
                InventoryUtil.closeInventory(player, plugin);
            });

            chestItemsMenu.addButton(teamButton);
        }

        player.openInventory(chestItemsMenu.getInventory());
    }
}
