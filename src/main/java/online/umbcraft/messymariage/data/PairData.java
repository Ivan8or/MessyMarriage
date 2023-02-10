package online.umbcraft.messymariage.data;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PairData {

    Optional<UUID> getPartner(UUID pair, UUID player);
    Optional<Set<UUID>> getMembers(UUID pair);


    static UUID pairID(UUID a, UUID b) {

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
