package online.umbcraft.messymarriage.data.memory;

import online.umbcraft.messymarriage.data.PairData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class MemoryPairData implements PairData {

    // all existing pairings
    final private Map<UUID, Set<UUID>> pairs = new ConcurrentHashMap<>();

    // all pairings which are marriages
    final private Set<UUID> marriageIDs = new ConcurrentSkipListSet<>();

    // relates all married players to the id of their marriage
    final private Map<UUID, UUID> playerMarriages = new ConcurrentHashMap<>();

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
    public Set<UUID> allMarriages() {
        return Set.copyOf(marriageIDs);
    }

    @Override
    public Map<UUID, Set<UUID>> allPairings() {
        return pairs.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> Set.copyOf(e.getValue())));
    }

    @Override
    public UUID pairID(UUID a, UUID b) {
        UUID pairID = PairData.generatePairID(a, b);
        pairs.putIfAbsent(pairID, Set.of(a,b));
        return pairID;
    }

    @Override
    public void marry(UUID pair) {

        if(marriageIDs.contains(pair))
            return;

        marriageIDs.add(pair);

        Set<UUID> members = pairs.get(pair);

        for(UUID member : members)
            playerMarriages.put(member, pair);
    }

    @Override
    public void unmarry(UUID pair) {

        if(!marriageIDs.contains(pair))
            return;

        marriageIDs.remove(pair);

        Set<UUID> members = pairs.get(pair);

        for(UUID member : members)
            playerMarriages.remove(member);
    }

    @Override
    public void setPairings(Map<UUID, Set<UUID>> pairings) {
        pairs.putAll(pairings);
    }

    @Override
    public void setMarriages(Set<UUID> marriages) {
        marriageIDs.addAll(marriages);

        for(UUID pair : marriages) {
            Set<UUID> members = pairs.get(pair);
            for(UUID member : members)
                playerMarriages.put(member, pair);
        }
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

    @Override
    public boolean pairExists(UUID pair) {
        return pairs.containsKey(pair);
    }
}
