package online.umbcraft.messymarriage.amiability.adjusters;

import online.umbcraft.messymarriage.amiability.LevelSanitizer;
import online.umbcraft.messymarriage.data.PairData;
import online.umbcraft.messymarriage.util.SafePlayerDistance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class AmiabilityByBed implements Listener {

    final private Plugin plugin;
    final private LevelSanitizer levelSanitizer;
    final private PairData pairs;

    final private int NON_MARRIAGE_EXP_GAIN = 300;
    final private int MARRIAGE_EXP_GAIN = 750;

    public AmiabilityByBed(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.levelSanitizer = levelSanitizer;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSleepWithOtherPlayer(final TimeSkipEvent e) {

        if(e.getSkipReason() != TimeSkipEvent.SkipReason.NIGHT_SKIP)
            return;

        Collection<? extends Player> players = e.getWorld().getPlayers();
        Set<UUID> processedPairs = new HashSet<>();

        for(Player p1 : players)
            for(Player p2: players)
                if(p1 != p2)
                    processPair(p1, p2, processedPairs);

        levelSanitizer.flush();

    }

    private void processPair(Player p1, Player p2, Set<UUID> processedPairs) {

        UUID pairID = pairs.pairID(p1.getUniqueId(), p2.getUniqueId());

        if(processedPairs.contains(pairID))
            return;

        processedPairs.add(pairID);

        if(SafePlayerDistance.distance(p1, p2) > 1)
            return;

        boolean married = pairs.isMarriage(pairID);
        int expAmount = married ? MARRIAGE_EXP_GAIN : NON_MARRIAGE_EXP_GAIN;

        levelSanitizer.adjustAmiability(pairID, expAmount, false);
    }
}
