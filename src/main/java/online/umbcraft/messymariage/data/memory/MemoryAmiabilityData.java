package online.umbcraft.messymariage.data.memory;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.util.ExpLevelConverter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryAmiabilityData implements AmiabilityData {

    final private Map<UUID, Integer> amiabilityExp = new ConcurrentHashMap<>();

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

    @Override
    public Map<UUID, Integer> allExps() {
        return Map.copyOf(amiabilityExp);
    }

}
