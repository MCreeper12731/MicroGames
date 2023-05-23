package net.skret.microgames.phases;

import net.skret.microgames.managers.PhaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class Phase implements Listener {

    protected final PhaseManager phaseManager;

    public Phase(PhaseManager phaseManager) {
        this.phaseManager = phaseManager;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract Phase getNextPhase();

}
