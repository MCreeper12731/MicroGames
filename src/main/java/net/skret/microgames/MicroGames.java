package net.skret.microgames;

import com.samjakob.spigui.SpiGUI;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIConfig;
import net.skret.microgames.listeners.*;
import net.skret.microgames.managers.*;
import net.skret.microgames.models.Position;
import net.skret.microgames.models.Team;
import net.skret.microgames.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MicroGames extends JavaPlugin {

    private CustomItemManager customItemManager;
    private GuiManager guiManager;
    private SpawnManager spawnManager;
    private ConfigManager configManager;
    private PlayerManager playerManager;
    private PhaseManager phaseManager;
    private ChestManager chestManager;
    private ChestVotingManager chestVotingManager;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig());
        registerCommands();
    }

    @Override
    public void onEnable() {

        CommandAPI.onEnable(this);
        SpiGUI spiGUI = new SpiGUI(this);

        this.configManager = new ConfigManager(this);
        this.chestVotingManager = new ChestVotingManager();
        this.playerManager = new PlayerManager(this);
        this.chestManager = new ChestManager(this, configManager);
        this.guiManager = new GuiManager(this, spiGUI, playerManager, chestVotingManager);
        this.customItemManager = new CustomItemManager(this, guiManager, chestManager);
        this.spawnManager = new SpawnManager(this);
        this.phaseManager = new PhaseManager(this, configManager, playerManager, customItemManager);

        spawnManager.loadSpawns();

        registerListeners(
                new CustomItemUsageListener(customItemManager),
                new HungerListener(),
                new RightClickFoodListener(),
                new DropItemListener(),
                new PlayerRightClickListener(),
                new EarthquakeTestListener(this)
        );

    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommands() {
        new CommandAPICommand("getspawnsetter")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    player.getInventory().addItem(customItemManager.getItem("spawn_setter").getItem());
                })
                .register();
        new CommandAPICommand("getkitselector")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    player.getInventory().addItem(customItemManager.getItem("kit_selector").getItem());
                })
                .register();
        new CommandAPICommand("getteamselector")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    player.getInventory().addItem(customItemManager.getItem("team_selector").getItem());
                })
                .register();
        new CommandAPICommand("selectkit")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    guiManager.openKitMenu(player);
                })
                .register();
        new CommandAPICommand("selectteam")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    guiManager.openTeamMenu(player);
                })
                .register();
        new CommandAPICommand("givekits")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    playerManager.giveItems();
                    player.sendMessage(Color.color("&aGiving kits to players!"));
                })
                .register();
        new CommandAPICommand("savespawns")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    spawnManager.saveSpawns();
                    player.sendMessage(Color.color("&aSaved spawns!"));
                })
                .register();
        new CommandAPICommand("loadspawns")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    spawnManager.loadSpawns();
                    player.sendMessage(Color.color("&aLoaded spawns!"));
                })
                .register();
        new CommandAPICommand("resetchests")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    chestManager.resetChests();
                    player.sendMessage(Color.color("&aChests reset!"));
                })
                .register();
        new CommandAPICommand("displayspawns")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    for (Team team : Team.values()) {
                        Material material = Material.getMaterial(team.name() + "_WOOL");
                        for (Position position : team.getSpawns()) {
                            player.getWorld().getBlockAt(position.getLocation(player.getWorld())).setType(material);
                        }
                    }
                })
                .register();

        CommandAPICommand reloadSubCommand = new CommandAPICommand("reload")
                .executes((sender, args) -> {
                    configManager.reload();
                });
        CommandAPICommand startSubCommand = new CommandAPICommand("start")
                .executes((sender, args) -> {
                    phaseManager.forceStart(sender);
                });
        CommandAPICommand stopSubCommand = new CommandAPICommand("stop")
                .executes((sender, args) -> {
                    phaseManager.forceStop(sender);
                });

        new CommandAPICommand("microgames")
                .withSubcommand(reloadSubCommand)
                .withSubcommand(startSubCommand)
                .withSubcommand(stopSubCommand)
                .register();
        new CommandAPICommand("showteams")
                .executes((sender, args) -> {
                    if (!(sender instanceof Player player)) return;
                    for (Team team : Team.values()) {
                        player.sendMessage(team.toString() + ": " + team.getPlayers());
                    }
                })
                .register();
    }
}
