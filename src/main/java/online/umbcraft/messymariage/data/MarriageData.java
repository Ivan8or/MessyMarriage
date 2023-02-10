package online.umbcraft.messymariage.data;

import java.util.*;

public interface MarriageData {


    boolean isMarriage(UUID pair);

    boolean isMarried(UUID player);

    Optional<UUID> getMarriageID(UUID player);

}
