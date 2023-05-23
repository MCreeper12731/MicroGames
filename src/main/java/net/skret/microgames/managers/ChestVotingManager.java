package net.skret.microgames.managers;

import net.skret.microgames.models.ChestItems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChestVotingManager {

    private final Map<UUID, ChestItems> chestItemsVotes = new HashMap<>();

    public void setEntry(UUID id, ChestItems vote) {
        chestItemsVotes.put(id, vote);
    }

    public ChestItems getMostVoted() {
        Map<ChestItems, Integer> votes = new HashMap<>();
        chestItemsVotes.forEach((playerId, chestItems) -> {
            if (!votes.containsKey(chestItems)) {
                votes.put(chestItems, 1);
                return;
            }
            votes.put(chestItems, votes.get(chestItems) + 1);
        });
        ChestItems mostVoted = null;
        for (ChestItems chestItems : votes.keySet()) {
            if (mostVoted == null) {
                mostVoted = chestItems;
                continue;
            }
            if (votes.get(chestItems) > votes.get(mostVoted)) mostVoted = chestItems;
        }
        return mostVoted;
    }

    public void clearEntries() {
        chestItemsVotes.clear();
    }

}
