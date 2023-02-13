package online.umbcraft.messymariage.amiability.adjusters;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.data.PairData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class AmiabilityByDamage implements Listener {

    final private Plugin plugin;
    final private LevelSanitizer levelSanitizer;
    final private PairData pairs;

    final private int NON_MARRIAGE_EXP_LOSS = -50;
    final private int MARRIAGE_EXP_LOSS = -200;


    public AmiabilityByDamage(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.levelSanitizer = levelSanitizer;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamagePlayer(final EntityDamageByEntityEvent e) {

        if(!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player))
            return;

        Player damager = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();

        UUID pairID = pairs.pairID(damager.getUniqueId(), victim.getUniqueId());

        boolean married = pairs.isMarriage(pairID);
        int expAmount = married ? MARRIAGE_EXP_LOSS : NON_MARRIAGE_EXP_LOSS;

        levelSanitizer.adjustAmiability(pairID, expAmount, true);
    }
}
