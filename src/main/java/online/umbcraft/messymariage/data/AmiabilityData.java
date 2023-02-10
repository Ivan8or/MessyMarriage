package online.umbcraft.messymariage.data;

import java.util.Optional;
import java.util.UUID;

public interface AmiabilityData {

    Optional<Integer> getAmiabilityLevel(UUID pair);

    Optional<Integer> getAmiabilityExp(UUID pair);

    void setExp(UUID pair, int amount);


}
