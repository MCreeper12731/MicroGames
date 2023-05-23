package net.skret.microgames.managers;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.PlayerWrapper;
import net.skret.microgames.models.Team;
import net.skret.microgames.models.kits.Kit;
import net.skret.microgames.models.kits.KitType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private final KitManager kitManager;
    private final Map<UUID, PlayerWrapper> players = new HashMap<>();

    public PlayerManager(MicroGames plugin) {
        this.kitManager = new KitManager(plugin, this);
    }

    //PLAYER MANIPULATION LOGIC

    public void addPlayer(UUID id) {
        PlayerWrapper player = new PlayerWrapper(id, Team.getTeam((int)(Math.random() * 4)));
        players.put(id, player);
        player.getTeam().addPlayer(id);
        player.setSelectedKit(kitManager.getKit(id, KitType.KNIGHT));
    }

    public void removePlayer(UUID id) {
        players.get(id).getTeam().removePlayer(id);
        players.remove(id);
    }

    public PlayerWrapper getPlayer(UUID id) {
        return players.get(id);
    }

    public Set<PlayerWrapper> getPlayers(Team team) {
        Set<PlayerWrapper> playersFromTeam = new HashSet<>();
        team.getPlayers().forEach(playerId -> playersFromTeam.add(players.get(playerId)));
        return playersFromTeam;
    }

    public Set<PlayerWrapper> getPlayers() {
        return ImmutableSet.copyOf(players.values());
    }

    //TEAM MANIPULATION LOGIC

    public Team getTeam(UUID id) {
        if (players.get(id) == null) return null;
        return players.get(id).getTeam();
    }

    public void setTeam(UUID id, Team team) {
        Team prevTeam = getTeam(id);
        if (prevTeam != null) prevTeam.removePlayer(id);
        players.get(id).setTeam(team);
        team.addPlayer(id);
    }

    //ALIVE CHECKS

    public void setAlive(UUID id, boolean alive) {
        players.get(id).setAlive(alive);
    }

    public boolean isAlive(UUID id) {
        return getPlayer(id).isAlive();
    }

    public Set<PlayerWrapper> getAlivePlayers(Team team) {
        Set<PlayerWrapper> players = getPlayers(team);
        players.removeIf(playerWrapper -> !playerWrapper.isAlive());
        return players;
    }

    public Set<PlayerWrapper> getAlivePlayers() {
        Set<PlayerWrapper> players = new HashSet<>();
        this.players.forEach((id, player) -> {
            if (player.isAlive()) players.add(player);
        });
        return players;
    }

    //KIT MANIPULATION LOGIC

    public void setKit(UUID id, KitType kitType) {
        getPlayer(id).setSelectedKit(kitManager.getKit(id, kitType));
    }

    public Kit getKit(UUID id) {
        return getPlayer(id).getSelectedKit();
    }

    public void giveItems() {
        kitManager.giveItems();
    }

    public void enableKits() {
        kitManager.enableListeners();
    }

    public void disableKits() {
        kitManager.disableListeners();
    }

}
