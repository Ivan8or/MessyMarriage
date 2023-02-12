package online.umbcraft.messymariage.amiability;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.data.PairData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import online.umbcraft.messymariage.util.MessageUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class LevelSanitizer {

    final private AmiabilityData amiabilities;
    final private PairData pairs;

    final static public int DEFAULT_LEVEL_AMOUNT = 0;
    final static public int DEFAULT_EXP_AMOUNT = ExpLevelConverter.toExp(DEFAULT_LEVEL_AMOUNT);

    final static public int MARRIAGE_LEVEL_LIMIT = 100;
    final static public int MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(MARRIAGE_LEVEL_LIMIT+1)-1;


    final static public int NON_MARRIAGE_LEVEL_LIMIT = 50;
    final static public int NON_MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(NON_MARRIAGE_LEVEL_LIMIT+1)-1;

    public LevelSanitizer(AmiabilityData amiabilities, PairData pairs) {
        this.amiabilities = amiabilities;
        this.pairs = pairs;
    }

    public int getLevel(final UUID pair) {
        Optional<Integer> potentialLevel = amiabilities.getAmiabilityLevel(pair);

        if(potentialLevel.isEmpty()) {
            amiabilities.setExp(pair, DEFAULT_EXP_AMOUNT);
            return DEFAULT_LEVEL_AMOUNT;
        }
        return potentialLevel.get();
    }

    // returns new exp amount for the pairing
    public int adjustAmiability(final UUID pair, final int increment) {

        Optional<Integer> result = amiabilities.getAmiabilityExp(pair);

        int currentEXP = result.orElse(DEFAULT_EXP_AMOUNT);
        int newEXP = currentEXP + increment;
        int currentLevel = ExpLevelConverter.toLevel(currentEXP);
        int newLevel = ExpLevelConverter.toLevel(newEXP);

        boolean married = pairs.isMarriage(pair);

        // if hit level cap
        if(newLevel > (married ? MARRIAGE_LEVEL_LIMIT : NON_MARRIAGE_LEVEL_LIMIT) ) {
            newEXP = (married ? MARRIAGE_EXP_LIMIT : NON_MARRIAGE_EXP_LIMIT);
            newLevel = (married ? MARRIAGE_LEVEL_LIMIT : NON_MARRIAGE_LEVEL_LIMIT);
        }
        amiabilities.setExp(pair, newEXP);

        if(newLevel == currentLevel)
            return newEXP;

        Optional<Set<UUID>> membersOptional = pairs.getMembers(pair);
        assert membersOptional.isPresent() : "player pairing does not exist";

        Set<UUID> members = membersOptional.get();
        List<OfflinePlayer> players =
                members.stream()
                .map(Bukkit::getOfflinePlayer)
                .collect(Collectors.toList());

        String levelChangePrefix = "&eYour amiability with ";
        String levelChangeSuffix;
        if(newLevel > currentLevel)
            levelChangeSuffix = " has &aincreased &efrom &6"+currentLevel+"&e to &6"+newLevel+"&e.";
        else
            levelChangeSuffix =  " has &cdecreased &efrom &6"+currentLevel+"&e to &6"+newLevel+"&e.";

        OfflinePlayer p1 = players.get(0);
        OfflinePlayer p2 = players.get(1);

        System.out.println(pair+levelChangeSuffix);

        if(p1.isOnline())
            MessageUI.sendActionbarMessage(p1.getPlayer(), levelChangePrefix + p2.getName() + levelChangeSuffix);
        if(p2.isOnline())
            MessageUI.sendActionbarMessage(p2.getPlayer(), levelChangePrefix + p1.getName() + levelChangeSuffix);

        return newEXP;
    }
}
