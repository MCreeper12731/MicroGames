package net.skret.microgames.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.skret.microgames.MicroGames;
import net.skret.microgames.models.Position;
import net.skret.microgames.models.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class SpawnManager {

    private final MicroGames plugin;

    public SpawnManager(MicroGames plugin) {
        this.plugin = plugin;
    }

    public void saveSpawns() {
        try {
            File file = new File(
                    plugin.getDataFolder().getAbsoluteFile() +
                            "/spawns.json"
            );
            file.getParentFile().mkdir();
            file.createNewFile();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            Writer writer = new FileWriter(file, false);
            List<Set<Position>> positions = new ArrayList<>();

            for (Team team : Team.values()) {
                positions.add(team.getSpawns());
            }

            gson.toJson(positions, writer);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadSpawns() {
        try {
            File file = new File(
                    plugin.getDataFolder().getAbsoluteFile() +
                            "/spawns.json"
            );
            if (!file.exists()) return;
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            Reader reader = new FileReader(file);
            Type listType = new TypeToken<List<List<Position>>>(){}.getType();
            List<List<Position>> spawns = gson.fromJson(reader, listType);
            for (int i = 0; i < spawns.size(); i++) {
                Team team = Team.values()[i];
                team.addSpawns(spawns.get(i));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
