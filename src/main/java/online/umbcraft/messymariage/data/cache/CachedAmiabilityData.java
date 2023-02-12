package online.umbcraft.messymariage.data.cache;

import online.umbcraft.messymariage.data.AmiabilityData;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CachedAmiabilityData implements AmiabilityData {

    final private AmiabilityData primary;
    final private AmiabilityData secondary;

    public CachedAmiabilityData(AmiabilityData primary, AmiabilityData secondary) {
        this.primary = primary;
        this.secondary = secondary;

        Map<UUID, Integer> allExps = secondary.allExps();

        for(Map.Entry<UUID,Integer> e : allExps.entrySet())
            primary.setExp(e.getKey(), e.getValue());

    }

    @Override
    public Optional<Integer> getAmiabilityLevel(UUID pair) {
        return primary.getAmiabilityLevel(pair);
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID pair) {
        return primary.getAmiabilityExp(pair);
    }

    @Override
    public void setExp(UUID pair, int amount) {
        primary.setExp(pair, amount);
        secondary.setExp(pair, amount);
    }

    @Override
    public void setManyExp(Map<UUID, Integer> toUpdate) {
        primary.setManyExp(toUpdate);
        secondary.setManyExp(toUpdate);
    }

    @Override
    public Map<UUID, Integer> allExps() {
        return primary.allExps();
    }
}
