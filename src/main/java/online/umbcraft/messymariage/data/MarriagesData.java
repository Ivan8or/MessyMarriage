package online.umbcraft.messymariage.data;

import java.util.*;

public abstract class MarriagesData {

    // relates all married players to the id of their marriage
    final protected Map<UUID, UUID> allPlayersMarriages = new HashMap<>();
    // all existing marriages
    final protected Set<UUID> allMarriageIDs = new HashSet<>();

    public abstract boolean areMarried(UUID pair);
    public abstract boolean areMarried(UUID a, UUID b);

    public boolean isMarriage(UUID pair) {
        return allMarriageIDs.contains(pair);
    }

}
