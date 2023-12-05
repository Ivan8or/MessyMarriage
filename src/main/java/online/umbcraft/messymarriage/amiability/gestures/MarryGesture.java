package online.umbcraft.messymarriage.amiability.gestures;

import online.umbcraft.messymarriage.amiability.LevelSanitizer;
import online.umbcraft.messymarriage.data.PairData;
import online.umbcraft.messymarriage.util.CustomItemIdentifier;
import online.umbcraft.messymarriage.util.MessageUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MarryGesture implements Listener {

    final private Plugin plugin;
    final private LevelSanitizer levelSanitizer;
    final private PairData pairs;

    final private static int MIN_PROPOSE_LEVEL = 50;

    final private Map<UUID, UUID> proposals = new HashMap<>();

    public MarryGesture(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
        this.plugin = plugin;
        this.pairs = pairs;
        this.levelSanitizer = levelSanitizer;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProposeToPlayer(final PlayerInteractEntityEvent e) {

        if(e.getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        if(!(e.getRightClicked() instanceof Player))
            return;
        
        Player suitor = e.getPlayer();
        Player proposee = (Player) e.getRightClicked();
        UUID pairID = pairs.pairID(suitor.getUniqueId(), proposee.getUniqueId());

        ItemStack itemInHand = suitor.getInventory().getItemInMainHand();

        if(!CustomItemIdentifier.hasTag(plugin, itemInHand, "wedding-item"))
            return;

        if(proposals.containsKey(proposee.getUniqueId()) &&
            proposals.get(proposee.getUniqueId()).equals(suitor.getUniqueId())) {

            pairs.marry(pairID);
            MessageUI.sendActionbarMessage(suitor,"&eYou have married "+proposee.getName()+"!");
            MessageUI.sendActionbarMessage(proposee,"&eYou have married "+suitor.getName()+"!");
            plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&e"+proposee.getName()+" and "+suitor.getName()+" have married!"));

            ItemStack toRemove = itemInHand.clone();
            toRemove.setAmount(1);
            suitor.getInventory().removeItem(toRemove);

            proposals.remove(proposee.getUniqueId());
            return;
        }

        if(!suitor.isSneaking())
            return;

        if(proposals.containsKey(suitor.getUniqueId())) {
            MessageUI.sendActionbarMessage(suitor, "&cWait a bit before proposing again.");
            return;
        }

        if(pairs.isMarriage(pairID)) {
            MessageUI.sendActionbarMessage(suitor, "&cYou are already married to " + proposee.getName() + "!");
            return;
        }

        if(pairs.isMarried(suitor.getUniqueId())) {
            MessageUI.sendActionbarMessage(suitor, "&cYou are already married!");
            return;
        }

        if(pairs.isMarried(proposee.getUniqueId())) {
            MessageUI.sendActionbarMessage(suitor, "&c"+proposee.getName()+" is already married!");
            return;
        }

        if(levelSanitizer.getLevel(pairID) < MIN_PROPOSE_LEVEL) {
            int curLevel = levelSanitizer.getLevel(pairID);
            MessageUI.sendActionbarMessage(suitor,
                    "&cYou must be at least amiability &6"+MIN_PROPOSE_LEVEL+"&e to propose. Yours is &6"+curLevel+"&e.");
            return;
        }

        proposals.put(suitor.getUniqueId(), proposee.getUniqueId());
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> proposals.remove(suitor.getUniqueId()), 20*60);

        MessageUI.sendActionbarMessage(suitor, "&eYou have proposed to "+proposee.getName()+".");
        MessageUI.sendChatMessage(proposee, "&e"+suitor.getName()+" has proposed to you. Will you exchange rings?");

        ItemStack toRemove = itemInHand.clone();
        toRemove.setAmount(1);
        suitor.getInventory().removeItem(toRemove);
    }

}
