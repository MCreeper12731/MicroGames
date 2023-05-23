package net.skret.microgames.models;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.skret.microgames.util.Color;

import java.util.*;

public enum Team {

    GREEN('2', 0, 170, 0),
    BLUE('1', 0, 0, 170),
    RED('c', 255, 85, 85),
    YELLOW('e', 255, 255, 85);

    @Getter
    private final char color;
    @Getter
    private final int[] rgbValue;
    private final Set<UUID> players = new HashSet<>();
    private final Set<Position> spawns = new HashSet<>();

    Team(char color, int... rgbValue) {
        this.color = color;
        this.rgbValue = rgbValue;
    }

    public static Team getTeam(int n) {
        return switch (n) {
            case 0 -> GREEN;
            case 1 -> BLUE;
            case 2 -> RED;
            case 3 -> YELLOW;
            default -> null;
        };
    }

    public static Team getTeam(UUID id) {
        for (Team team : Team.values()) {
            if (team.players.contains(id)) return team;
        }
        return null;
    }

    @Override
    public String toString() {
        return "&" + color + this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }

    public void addPlayer(UUID id) {
        players.add(id);
    }

    public void addPlayers(List<UUID> ids) {
        players.addAll(ids);
    }

    public void removePlayer(UUID id) {
        players.remove(id);
    }

    public Set<UUID> getPlayers() {
        return ImmutableSet.copyOf(players);
    }

    public void clearPlayers() {
        this.players.clear();
    }

    public void addSpawn(Position spawn) {
        spawns.add(spawn);
    }

    public void addSpawns(List<Position> spawns) {
        this.spawns.addAll(spawns);
    }

    public Set<Position> getSpawns() {
        return ImmutableSet.copyOf(spawns);
    }

}
