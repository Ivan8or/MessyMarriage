package online.umbcraft.messymariage.data;

import java.util.*;

public abstract class MarriagesData {

    // relates all married players to the id of their marriage
    final protected Map<UUID, UUID> player = new HashMap<>();

    // all existing marriages
    final protected Set<UUID> marriages = new HashSet<>();

    public abstract boolean areMarried(UUID a, UUID b);

    public boolean isMarriage(UUID pair) {
        return marriages.contains(pair);
    }

    public static UUID pairID(UUID a, UUID b) {

        // upper halves of each uuid
        long ah = a.getMostSignificantBits();
        long bh = b.getMostSignificantBits();

        // lower halves of each uuid
        long al = a.getLeastSignificantBits();
        long bl = b.getLeastSignificantBits();

        // long multiplication conveniently overflows so as to fit perfectly into the result UUID
        return new UUID(al*bl, ah*bh);
    }
}
