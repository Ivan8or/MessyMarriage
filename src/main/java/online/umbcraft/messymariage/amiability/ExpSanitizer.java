package online.umbcraft.messymariage.amiability;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import online.umbcraft.messymariage.util.PlayerPair;

import java.util.Optional;
import java.util.UUID;

public class ExpSanitizer {

    final private AmiabilityData storage;
    final private int DEFAULT_EXP_AMOUNT = ExpLevelConverter.toExp(20);

    final private int NON_MARRIAGE_LEVEL_LIMIT = 50;
    final private int NON_MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(NON_MARRIAGE_LEVEL_LIMIT);

    public ExpSanitizer(AmiabilityData storage) {
        this.storage = storage;
    }

    public int adjustAmiability(final UUID pair, final int increment) {
        Optional<Integer> result = storage.getAmiabilityExp(pair);
        int currentAmount = result.orElse(DEFAULT_EXP_AMOUNT);
        int newAmount = currentAmount + increment;

        if(ExpLevelConverter.toLevel(newAmount) > NON_MARRIAGE_LEVEL_LIMIT) {
            newAmount = NON_MARRIAGE_EXP_LIMIT;
        }

        storage.setExp(pair, newAmount);

        return newAmount;
    }

    public int adjustAmiability(final UUID a, final UUID b, final int amount) {
        return adjustAmiability(PlayerPair.pairID(a,b), amount);
    }
}
