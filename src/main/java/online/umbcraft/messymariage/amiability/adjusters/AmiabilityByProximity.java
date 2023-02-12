package online.umbcraft.messymariage.amiability.adjusters;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.data.PairData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AmiabilityByProximity {

    final private static long TICKS_PER_SECOND = 20;

    final private Plugin plugin;
    final private LevelSanitizer levelSanitizer;
    final private PairData pairs;

    final private static double DISTANCE_THRESHOLD = 48;

    public AmiabilityByProximity(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.levelSanitizer = levelSanitizer;
    }

    public void start() {

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        long delay = 0L;
        long period = TICKS_PER_SECOND * 60;

        Runnable task = () -> {
            Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
            Set<UUID> processedPairs = new HashSet<>();

            for(Player p1 : players) {
                for(Player p2: players) {
                    if(p1 == p2)
                        continue;

                    processPair(p1, p2, processedPairs);
                }
            }
        };

        scheduler.runTaskTimer(plugin, task, delay, period);
    }

    public void processPair(Player p1, Player p2, Set<UUID> processedPairs) {
        UUID pairID = pairs.pairID(p1.getUniqueId(), p2.getUniqueId());

        if(processedPairs.contains(pairID))
            return;

        double distance = p1.getLocation().distance(p2.getLocation());
        boolean married = pairs.isMarriage(pairID);

        int positiveAdjust = married ? 7 : 5;
        int negativeAdjust = married ? -3 : -1;

        if(distance < DISTANCE_THRESHOLD)
            levelSanitizer.adjustAmiability(pairID, positiveAdjust);
        else
            levelSanitizer.adjustAmiability(pairID, negativeAdjust);

        processedPairs.add(pairID);
    }

}
