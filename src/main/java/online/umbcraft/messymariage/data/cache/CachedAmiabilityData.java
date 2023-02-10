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

        Optional<Integer> primaryLevel = primary.getAmiabilityLevel(pair);

        if(primaryLevel.isPresent())
            return primaryLevel;


        Optional<Integer> exp = secondary.getAmiabilityExp(pair);
        if(exp.isEmpty())
            return Optional.empty();

        primary.setExp(pair, exp.get());
        int level = ExpLevelConverter.toLevel(exp.get());
        return Optional.of(level);
    }

    @Override
    public Optional<Integer> getAmiabilityExp(UUID pair) {

        Optional<Integer> primaryExp = primary.getAmiabilityExp(pair);

        if(primaryExp.isPresent())
            return primaryExp;

        Optional<Integer> secondaryExp = secondary.getAmiabilityExp(pair);

        if(secondaryExp.isEmpty())
            return Optional.empty();

        primary.setExp(pair, secondaryExp.get());
        return secondaryExp;
    }

    @Override
    public void setExp(UUID pair, int amount) {
        primary.setExp(pair, amount);
        secondary.setExp(pair, amount);
    }
}
