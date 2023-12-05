package online.umbcraft.messymarriage.amiability.adjusters;

import online.umbcraft.messymarriage.amiability.LevelSanitizer;
import online.umbcraft.messymarriage.data.PairData;
import online.umbcraft.messymarriage.util.SafePlayerDistance;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class AmiabilityByFood implements Listener {

    final private Plugin plugin;
    final private LevelSanitizer levelSanitizer;
    final private PairData pairs;

    final private int NON_MARRIAGE_EXP_GAIN = 30;
    final private int MARRIAGE_EXP_GAIN = 100;

    final private int SECONDS_COOLDOWN = 60;
    final private int MAX_DISTANCE = 5;

    final private Set<UUID> pairsOnCooldown = new HashSet<>();
    final private Map<UUID, Location> justEaten = new HashMap<>();

    public AmiabilityByFood(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.levelSanitizer = levelSanitizer;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEatWithAnotherPlayer(final FoodLevelChangeEvent e) {
        if(e.getItem() == null)
            return;

        if(!(e.getEntity() instanceof Player))
            return;

        Player p = (Player) e.getEntity();
        final UUID playerUUID = p.getUniqueId();
        Location ateAt = p.getLocation();

        for(Map.Entry<UUID, Location> entry : justEaten.entrySet()) {

            if(SafePlayerDistance.distance(entry.getValue(), ateAt) > MAX_DISTANCE)
                continue;

           processPair(playerUUID, entry.getKey());
        }

        justEaten.put(playerUUID, ateAt);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> justEaten.remove(playerUUID), 25);

        levelSanitizer.flush();
    }


    private void processPair(UUID p1, UUID p2) {

        UUID pairID = pairs.pairID(p1, p2);

        if(pairsOnCooldown.contains(pairID))
            return;

        if(p1.equals(p2))
            return;

        pairsOnCooldown.add(pairID);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> pairsOnCooldown.remove(pairID), 20 * SECONDS_COOLDOWN);

        boolean married = pairs.isMarriage(pairID);
        int expAmount = married ? MARRIAGE_EXP_GAIN : NON_MARRIAGE_EXP_GAIN;


        levelSanitizer.adjustAmiability(pairID, expAmount, false);


    }

}
