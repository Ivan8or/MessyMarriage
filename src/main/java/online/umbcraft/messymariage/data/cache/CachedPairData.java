package online.umbcraft.messymariage.data.cache;

import online.umbcraft.messymariage.data.PairData;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CachedPairData implements PairData {

    final private PairData primary;
    final private PairData secondary;

    public CachedPairData(PairData primary, PairData secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Optional<UUID> getPartner(UUID pair, UUID player) {
        return primary.getPartner(pair, player);
    }

    @Override
    public Optional<Set<UUID>> getMembers(UUID pair) {
        return primary.getMembers(pair);
    }

    @Override
    public boolean pairExists(UUID pair) {
        return false;
    }

    @Override
    public boolean isMarriage(UUID pair) {
        return primary.isMarriage(pair);
    }

    @Override
    public boolean isMarried(UUID player) {
        return primary.isMarried(player);
    }

    @Override
    public Optional<UUID> getMarriageID(UUID player) {
        return primary.getMarriageID(player);
    }

    @Override
    public Set<UUID> allMarriages() {
        return primary.allMarriages();
    }

    @Override
    public Map<UUID, Set<UUID>> allPairings() {
        return primary.allPairings();
    }

    @Override
    public UUID pairID(UUID a, UUID b) {
        UUID pairID = PairData.generatePairID(a, b);

        if(!primary.pairExists(pairID))
            primary.pairID(a,b);
            secondary.pairID(a,b);

        return pairID;
    }

    @Override
    public void marry(UUID pair) {
        primary.marry(pair);
        secondary.marry(pair);
    }

    @Override
    public void unmarry(UUID pair) {
        primary.unmarry(pair);
        secondary.unmarry(pair);
    }
}
