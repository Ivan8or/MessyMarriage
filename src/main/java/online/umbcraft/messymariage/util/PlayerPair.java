package online.umbcraft.messymariage.util;

import java.util.UUID;

public class PlayerPair {
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
