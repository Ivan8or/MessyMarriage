package online.umbcraft.messymarriage.crafting.listeners;

import online.umbcraft.messymarriage.util.CustomItemIdentifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CancelCustomItemsUse implements Listener {

    final private Plugin plugin;

    public CancelCustomItemsUse(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAttemptPlaceItem(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if(CustomItemIdentifier.isCustom(plugin, item))
            e.setCancelled(true);
    }


}
