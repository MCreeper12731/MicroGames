package net.skret.microgames.managers;

import lombok.Getter;
import net.skret.microgames.MicroGames;
import net.skret.microgames.phases.ActivePhase;
import net.skret.microgames.phases.Phase;
import net.skret.microgames.phases.WaitingPhase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PhaseManager {

    @Getter
    private final MicroGames plugin;
    @Getter
    private final ConfigManager configManager;
    @Getter
    private final PlayerManager playerManager;
    @Getter
    private final CustomItemManager customItemManager;

    private Phase currentPhase;

    public PhaseManager(MicroGames plugin,
                        ConfigManager configManager,
                        PlayerManager playerManager,
                        CustomItemManager customItemManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.playerManager = playerManager;
        this.customItemManager = customItemManager;
        setPhase(new WaitingPhase(this));
    }

    public Class<? extends Phase> getPhase() {
        return currentPhase.getClass();
    }

    public boolean isInPhase(Class<? extends Phase> phaseClass) {
        return getPhase().equals(phaseClass);
    }

    public void nextPhase() {
        setPhase(currentPhase.getNextPhase());
    }

    private void setPhase(Phase phase) {
        if (currentPhase != null) {
            currentPhase.onDisable();
            HandlerList.unregisterAll(currentPhase);
        }

        currentPhase = phase;
        Bukkit.getPluginManager().registerEvents(currentPhase, plugin);
        currentPhase.onEnable();
    }

    public void forceStart(CommandSender sender) {
        if (!isInPhase(WaitingPhase.class)) {
            sender.sendMessage("Game cannot be force started at the moment!");
            return;
        }
        nextPhase();
    }

    public void forceStop(CommandSender sender) {
        if (!isInPhase(ActivePhase.class)) {
            sender.sendMessage("Game cannot be force stopped at the moment!");
            return;
        }
        nextPhase();
    }

}
