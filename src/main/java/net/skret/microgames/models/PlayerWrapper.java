package net.skret.microgames.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import net.skret.microgames.models.kits.Kit;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerWrapper {

    private final UUID id;
    private Team team;
    private Kit selectedKit;
    private boolean alive = true;

    public PlayerWrapper(UUID id, Team team) {
        this.id = id;
        this.team = team;
    }

}
