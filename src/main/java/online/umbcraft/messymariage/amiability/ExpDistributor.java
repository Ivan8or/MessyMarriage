package online.umbcraft.messymariage.amiability;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.data.MarriageData;
import online.umbcraft.messymariage.data.PairData;
import online.umbcraft.messymariage.util.ExpLevelConverter;

import java.util.Optional;
import java.util.UUID;

public class ExpDistributor {

    final private AmiabilityData amiabilities;
    final private MarriageData marriages;

    final private int DEFAULT_EXP_AMOUNT = ExpLevelConverter.toExp(20);

    final private int MARRIAGE_LEVEL_LIMIT = 100;
    final private int MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(MARRIAGE_LEVEL_LIMIT);


    final private int NON_MARRIAGE_LEVEL_LIMIT = 50;
    final private int NON_MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(NON_MARRIAGE_LEVEL_LIMIT);

    public ExpDistributor(AmiabilityData amiabilities, MarriageData marriages) {
        this.amiabilities = amiabilities;
        this.marriages = marriages;
    }

    public int adjustAmiability(final UUID pair, final int increment) {
        Optional<Integer> result = amiabilities.getAmiabilityExp(pair);

        boolean married = marriages.isMarriage(pair);

        int currentAmount = result.orElse(DEFAULT_EXP_AMOUNT);
        int newAmount = currentAmount + increment;

        int currentLevel = ExpLevelConverter.toLevel(currentAmount);
        int newLevel = ExpLevelConverter.toLevel(newAmount);

        // if hit level cap
        if(newLevel > (married ? MARRIAGE_LEVEL_LIMIT : NON_MARRIAGE_LEVEL_LIMIT) ) {
            newAmount = (married ? MARRIAGE_EXP_LIMIT : NON_MARRIAGE_EXP_LIMIT) ;
        }
        // did not hit level cap
        else if(newLevel > currentLevel) {
            // TODO send notification to each player about changing amiability level
        }

        amiabilities.setExp(pair, newAmount);

        return newAmount;
    }

    public int adjustAmiability(final UUID a, final UUID b, final int amount) {
        return adjustAmiability(PairData.pairID(a,b), amount);
    }
}
