package online.umbcraft.messymarriage.data;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PairData {

    Optional<UUID> getPartner(UUID pair, UUID player);

    Optional<Set<UUID>> getMembers(UUID pair);

    boolean pairExists(UUID pair);

    boolean isMarriage(UUID pair);

    boolean isMarried(UUID player);

    Optional<UUID> getMarriageID(UUID player);

    Set<UUID> allMarriages();

    Map<UUID, Set<UUID>> allPairings();

    UUID pairID(UUID a, UUID b);

    void marry(UUID pair);

    void unmarry(UUID pair);

    void setPairings(Map<UUID, Set<UUID>> pairings);

    void setMarriages(Set<UUID> marriages);

    static UUID generatePairID(UUID a, UUID b) {

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
