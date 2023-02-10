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
    public Optional<Integer> getAmiabilityLevel(UUID a, UUID b) {
        return getAmiabilityLevel(PlayerPair.pairID(a,b));
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID pair) {
        if(!amiabilityExp.containsKey(pair))
            return Optional.empty();

        int exp = amiabilityExp.get(pair);
        return Optional.of(exp);
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID a, UUID b) {
        return getAmiabilityExp(PlayerPair.pairID(a,b));
    }

    @Override
    public Optional<Integer> alterExp(UUID pair, int amount) {
        if(!amiabilityExp.containsKey(pair))
            return Optional.empty();

        int oldExp = amiabilityExp.get(pair);
        int newExp = oldExp + amount;

        amiabilityExp.put(pair, newExp);
        return Optional.of(newExp);
    }

    @Override
    public Optional<Integer> alterExp(UUID a, UUID b, int amount) {
        return alterExp(PlayerPair.pairID(a,b), amount);
    }

    @Override
    public void setExp(UUID pair, int amount) {
        amiabilityExp.put(pair, amount);
    }

    @Override
    public void setExp(UUID a, UUID b, int amount) {
        setExp(PlayerPair.pairID(a,b), amount);
    }

    @Override
    public boolean hasPair(UUID pair) {
        return amiabilityExp.containsKey(pair);
    }

    @Override
    public boolean hasPair(UUID a, UUID b) {
        return hasPair(PlayerPair.pairID(a,b));
    }
}
