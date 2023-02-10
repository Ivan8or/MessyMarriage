package online.umbcraft.messymariage.data.memory;

import online.umbcraft.messymariage.data.PairData;

import java.util.*;

public class MemoryPairData implements PairData {

    final private Map<UUID, Set<UUID>> pairs = new HashMap<>();

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
