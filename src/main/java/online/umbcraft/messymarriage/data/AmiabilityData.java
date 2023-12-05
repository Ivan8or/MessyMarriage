package online.umbcraft.messymarriage.data;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface AmiabilityData {

    Optional<Integer> getAmiabilityLevel(UUID pair);

    Optional<Integer> getAmiabilityExp(UUID pair);

    void setExp(UUID pair, int amount);

    void setManyExp(Map<UUID, Integer> toUpdate);

    Map<UUID, Integer> allExps();

}
