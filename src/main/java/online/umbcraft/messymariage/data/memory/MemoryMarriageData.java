package online.umbcraft.messymariage.data.memory;

import online.umbcraft.messymariage.data.MarriageData;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryMarriageData implements MarriageData {

    // relates all married players to the id of their marriage
    final protected Map<UUID, UUID> playerToMarriage = new HashMap<>();
    // all existing marriages
    final protected Map<UUID, Set<UUID>> marriageToPlayers = new HashMap<>();

    @Override
    public boolean isMarriage(UUID pair) {
        return marriageToPlayers.containsKey(pair);
    }

    @Override
    public boolean isMarried(UUID player) {
        return playerToMarriage.containsKey(player);
    }

    @Override
    public Optional<UUID> getMarriageID(UUID player) {
        UUID potentialMarriageID = playerToMarriage.get(player);
        if(potentialMarriageID == null)
            return Optional.empty();

        return Optional.of(potentialMarriageID);
    }

//    @Override
//    public Optional<UUID> getWhoMarriedTo(UUID player) {
//        UUID potentialMarriageID = playerToMarriage.get(player);
//        if(potentialMarriageID == null)
//            return Optional.empty();
//
//        Set<UUID> members = marriageToPlayers.get(potentialMarriageID);
//
//        Optional<UUID> partner =
//                members.stream()
//                .filter(uuid -> uuid != potentialMarriageID)
//                .findFirst();
//
//        return partner;
//    }
//
//    @Override
//    public Optional<Set<UUID>> getMembers(UUID marriage) {
//        Set<UUID> potentialMembers = Set.copyOf(marriageToPlayers.get(marriage));
//        if(potentialMembers == null)
//            return Optional.empty();
//
//        return Optional.of(potentialMembers);
//    }
}
