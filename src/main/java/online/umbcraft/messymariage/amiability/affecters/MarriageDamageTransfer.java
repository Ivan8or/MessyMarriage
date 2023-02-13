package online.umbcraft.messymariage.amiability.affecters;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.data.PairData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MarriageDamageTransfer implements Listener {

    final private Plugin plugin;
    final private LevelSanitizer levelSanitizer;
    final private PairData pairs;

    public MarriageDamageTransfer(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.levelSanitizer = levelSanitizer;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamagePlayer(final EntityDamageEvent e) {

        if(!(e.getEntity() instanceof Player))
            return;

        Player victim = (Player) e.getEntity();
        UUID victimID = e.getEntity().getUniqueId();

        if(!pairs.isMarried(victimID))
            return;

        UUID marriageID = pairs.getMarriageID(victimID).get();
        UUID partnerID = pairs.getPartner(marriageID, victimID).get();

        Player partnerPlayer = plugin.getServer().getPlayer(partnerID);
        if(partnerPlayer == null)
            return;

        int marriageLevel = levelSanitizer.getLevel(marriageID);

        double originalDamage = e.getFinalDamage();
        double reduceMult = damageReductionAmount(marriageLevel);


        double excess = Math.max(0, Math.min(originalDamage * reduceMult, partnerPlayer.getHealth()-1));
        double reducedDamage = originalDamage - excess;

        e.setDamage(0);
        victim.setHealth(Math.max(0, victim.getHealth() - reducedDamage));
        partnerPlayer.setHealth(partnerPlayer.getHealth() - excess);
    }

    private static double damageReductionAmount(int level) {
        return level * 0.005;
    }
}
