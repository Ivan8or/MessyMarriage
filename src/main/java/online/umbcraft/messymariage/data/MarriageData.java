package online.umbcraft.messymariage.data;

import java.util.*;

public interface MarriageData {


    boolean areMarried(UUID pair);
    boolean areMarried(UUID a, UUID b);

    boolean isMarried(UUID player);

    Optional<UUID> getMarriageID(UUID player);

    Optional<UUID> getMarriedTo(UUID player);

    Optional<Set<UUID>> getMembers(UUID marriage);


}
