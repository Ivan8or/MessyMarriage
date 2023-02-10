package online.umbcraft.messymariage.data.memory;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import online.umbcraft.messymariage.util.PlayerPair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MemoryAmiabilityData implements AmiabilityData {

    final protected Map<UUID, Integer> amiabilityExp = new HashMap<>();

    @Override
    public Optional<Integer> getAmiabilityLevel(UUID pair) {
        if(!amiabilityExp.containsKey(pair))
            return Optional.empty();

        int level = ExpLevelConverter.toLevel(amiabilityExp.get(pair));
        return Optional.of(level);
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID pair) {
        if(!amiabilityExp.containsKey(pair))
            return Optional.empty();

        int exp = amiabilityExp.get(pair);
        return Optional.of(exp);
    }

    @Override
    public void setExp(UUID pair, int amount) {
        amiabilityExp.put(pair, amount);
    }

}
