package online.umbcraft.messymariage.data.memory;

import online.umbcraft.messymariage.data.PairData;

import java.util.*;

public class MemoryPairData implements PairData {

    // all existing pairings
    final private Map<UUID, Set<UUID>> pairs = new HashMap<>();

    final private Set<UUID> marriageIDs = new HashSet<>();

    // relates all married players to the id of their marriage
    final private Map<UUID, UUID> playerMarriages = new HashMap<>();


    @Override
    public boolean isMarriage(UUID pair) {
        return marriageIDs.contains(pair);
    }

    @Override
    public boolean isMarried(UUID player) {
        return playerMarriages.containsKey(player);
    }

    @Override
    public Optional<UUID> getMarriageID(UUID player) {
        UUID potentialMarriageID = playerMarriages.get(player);
        if(potentialMarriageID == null)
            return Optional.empty();

        return Optional.of(potentialMarriageID);
    }

    @Override
    public Optional<UUID> getPartner(UUID pair, UUID player) {

        Set<UUID> members = pairs.get(pair);
        if(members == null)
            return Optional.empty();

        Optional<UUID> partner =
                members.stream()
                .filter(uuid -> uuid != player)
                .findFirst();

        return partner;
    }

    @Override
    public Optional<Set<UUID>> getMembers(UUID pair) {
        Set<UUID> members = pairs.get(pair);
        if(members == null)
            return Optional.empty();

        return Optional.of(members);
    }
}
