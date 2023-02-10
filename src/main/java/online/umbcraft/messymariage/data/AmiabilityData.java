package online.umbcraft.messymariage.data;

import java.util.Optional;
import java.util.UUID;

public interface AmiabilityData {

    Optional<Integer> getAmiabilityLevel(UUID pair);
    Optional<Integer> getAmiabilityLevel(UUID a, UUID b);

    Optional<Integer> getAmiabilityExp(UUID pair);
    Optional<Integer> getAmiabilityExp(UUID a, UUID b);

    void setExp(UUID pair, int amount);
    void setExp(UUID a, UUID b, int amount);
}
