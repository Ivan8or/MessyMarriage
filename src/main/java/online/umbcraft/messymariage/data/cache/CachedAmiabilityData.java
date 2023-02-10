package online.umbcraft.messymariage.data.cache;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import online.umbcraft.messymariage.util.PlayerPair;

import java.util.Optional;
import java.util.UUID;

public class CachedAmiabilityData implements AmiabilityData {

    final private AmiabilityData primary;
    final private AmiabilityData secondary;

    public CachedAmiabilityData(AmiabilityData primary, AmiabilityData secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Optional<Integer> getAmiabilityLevel(UUID pair) {
        if(primary.hasPair(pair))
            return primary.getAmiabilityLevel(pair);

        Optional<Integer> exp = secondary.getAmiabilityExp(pair);
        if(exp.isEmpty())
            return Optional.empty();

        primary.setExp(pair, exp.get());
        int level = ExpLevelConverter.toLevel(exp.get());
        return Optional.of(level);
    }

    @Override
    public Optional<Integer> getAmiabilityLevel(UUID a, UUID b) {
        return getAmiabilityLevel(PlayerPair.pairID(a,b));
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID pair) {
        if(primary.hasPair(pair))
            return primary.getAmiabilityExp(pair);

        Optional<Integer> exp = secondary.getAmiabilityExp(pair);
        if(exp.isEmpty())
            return Optional.empty();

        primary.setExp(pair, exp.get());
        return exp;
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID a, UUID b) {
        return getAmiabilityExp(PlayerPair.pairID(a,b));
    }

    @Override
    public Optional<Integer> alterExp(UUID pair, int amount) {
        Optional<Integer> exp = secondary.alterExp(pair, amount);
        primary.setExp(pair, exp.get());
        return exp;
    }

    @Override
    public Optional<Integer> alterExp(UUID a, UUID b, int amount) {
        return alterExp(PlayerPair.pairID(a,b), amount);
    }

    @Override
    public void setExp(UUID pair, int amount) {
        primary.setExp(pair, amount);
        secondary.setExp(pair, amount);
    }

    @Override
    public void setExp(UUID a, UUID b, int amount) {
        setExp(PlayerPair.pairID(a,b), amount);
    }

    @Override
    public boolean hasPair(UUID pair) {
        return primary.hasPair(pair) || secondary.hasPair(pair);
    }

    @Override
    public boolean hasPair(UUID a, UUID b) {
        return hasPair(PlayerPair.pairID(a,b));
    }
}
