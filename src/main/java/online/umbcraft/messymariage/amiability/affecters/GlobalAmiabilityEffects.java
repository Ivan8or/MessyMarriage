package online.umbcraft.messymariage.amiability.affecters;

import online.umbcraft.messymariage.amiability.ExpDistributor;
import online.umbcraft.messymariage.data.AmiabilityData;
import online.umbcraft.messymariage.data.PairData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.UUID;

public class GlobalAmiabilityEffects implements Listener {

    final private Plugin plugin;
    final private AmiabilityData amiabilities;

    public GlobalAmiabilityEffects(Plugin plugin, AmiabilityData amiabilities) {
        this.plugin = plugin;
        this.amiabilities = amiabilities;
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

        UUID pairID = PairData.pairID(damager.getUniqueId(), victim.getUniqueId());
        Optional<Integer> levelOptional = amiabilities.getAmiabilityLevel(pairID);

        int level = levelOptional.orElse(ExpDistributor.DEFAULT_LEVEL_AMOUNT);

        if(level <= 15) {
            Vector victimLoc = victim.getLocation().toVector();
            Vector damagerLoc = damager.getLocation().toVector();
            Vector velocityOffset = victimLoc.subtract(damagerLoc).normalize().multiply(0.3);

            victim.setVelocity(victim.getVelocity().add(velocityOffset));
        }
        else if(level >= 36) {
            e.setDamage(e.getDamage() * 0.85);
        }
    }


}
