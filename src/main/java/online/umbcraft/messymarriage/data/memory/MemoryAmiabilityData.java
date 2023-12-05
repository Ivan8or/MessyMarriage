package online.umbcraft.messymarriage.data.memory;

import online.umbcraft.messymarriage.data.AmiabilityData;
import online.umbcraft.messymarriage.util.ExpLevelConverter;

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
    public void setManyExp(Map<UUID, Integer> toUpdate) {
        for(Map.Entry<UUID, Integer> entry : toUpdate.entrySet())
            amiabilityExp.put(entry.getKey(), entry.getValue());

    }

    @Override
    public Map<UUID, Integer> allExps() {
        return Map.copyOf(amiabilityExp);
    }

}
