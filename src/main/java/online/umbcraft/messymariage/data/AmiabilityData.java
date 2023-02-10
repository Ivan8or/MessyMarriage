package online.umbcraft.messymariage.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class AmiabilityData {

    final protected Map<UUID, Double> amiabilities = new HashMap<>();

    public double getAmiability(UUID a, UUID b) {
        return getAmiability(MarriagesData.pairID(a,b));
    }
    public abstract double getAmiability(UUID pair);

}
