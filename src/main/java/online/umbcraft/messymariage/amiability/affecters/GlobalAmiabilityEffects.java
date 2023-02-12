package online.umbcraft.messymariage.amiability.affecters;

import online.umbcraft.messymariage.amiability.LevelSanitizer;

import online.umbcraft.messymariage.data.PairData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GlobalAmiabilityEffects implements Listener {

    final private Plugin plugin;
    final private LevelSanitizer exp;
    final private PairData pairs;

    public GlobalAmiabilityEffects(Plugin plugin, LevelSanitizer exp, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.exp = exp;
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
        int level = exp.getLevel(pairID);

        if(level <= 5) {
            Vector victimLoc = victim.getLocation().toVector();
            Vector damagerLoc = damager.getLocation().toVector();
            Vector velocityOffset = victimLoc.subtract(damagerLoc).normalize().multiply(0.3);

            victim.setVelocity(victim.getVelocity().add(velocityOffset));
        }
        else if(level >= 30) {
            e.setDamage(e.getDamage() * 0.85);
        }
    }


}
