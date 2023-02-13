package online.umbcraft.messymariage.amiability.affecters;

import online.umbcraft.messymariage.amiability.LevelSanitizer;
import online.umbcraft.messymariage.data.PairData;
import online.umbcraft.messymariage.util.CustomItemIdentifier;
import online.umbcraft.messymariage.util.MessageUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class DivorceGesture implements Listener {

    final private Plugin plugin;
    final private PairData pairs;
    final private LevelSanitizer levelSanitizer;

    public DivorceGesture(Plugin plugin, LevelSanitizer levelSanitizer, PairData pairs) {
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

        if(!CustomItemIdentifier.hasTag(plugin, itemInHand, "divorce-item"))
            return;

        if(!pairs.isMarriage(pairID)) {
            MessageUI.sendActionbarMessage(suitor, "&cYou are not married to " + proposee.getName() + "!");
            return;
        }

        ItemStack toRemove = itemInHand.clone();
        toRemove.setAmount(1);
        suitor.getInventory().removeItem(toRemove);

        pairs.unmarry(pairID);

        MessageUI.sendActionbarMessage(suitor,"&cYou have divorced "+proposee.getName()+"!");
        MessageUI.sendActionbarMessage(proposee,"&cYou have divorced "+suitor.getName()+"!");
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                "&c"+proposee.getName()+" and "+suitor.getName()+" have divorced!"));

        levelSanitizer.adjustAmiability(pairID, -1 * levelSanitizer.getExp(pairID));

    }
}
