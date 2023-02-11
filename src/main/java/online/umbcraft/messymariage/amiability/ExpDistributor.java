package online.umbcraft.messymariage.amiability;

import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.data.PairData;
import online.umbcraft.messymariage.util.ExpLevelConverter;
import online.umbcraft.messymariage.util.MessageUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExpDistributor {

    final private AmiabilityData amiabilities;
    final private PairData pairs;

    final private int DEFAULT_EXP_AMOUNT = ExpLevelConverter.toExp(20);

    final private int MARRIAGE_LEVEL_LIMIT = 100;
    final private int MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(MARRIAGE_LEVEL_LIMIT);


    final private int NON_MARRIAGE_LEVEL_LIMIT = 50;
    final private int NON_MARRIAGE_EXP_LIMIT = ExpLevelConverter.toExp(NON_MARRIAGE_LEVEL_LIMIT);

    public ExpDistributor(AmiabilityData amiabilities, PairData pairs) {
        this.amiabilities = amiabilities;
        this.pairs = pairs;
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

        String prefix = "Your amiability with ";
        String suffix = " has "+(newLevel > currentLevel ? "increased" : "decreased")+" from "+currentLevel+" to "+newLevel+".";

        OfflinePlayer p1 = players.get(0);
        OfflinePlayer p2 = players.get(1);

        if(p1.isOnline())
            MessageUI.sendActionbarMessage(p1.getPlayer(), prefix+p2.getName()+suffix);
        if(p2.isOnline())
            MessageUI.sendActionbarMessage(p2.getPlayer(), prefix+p1.getName()+suffix);

        return newEXP;
    }
}
