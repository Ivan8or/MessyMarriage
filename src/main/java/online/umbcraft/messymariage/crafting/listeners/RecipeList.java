package online.umbcraft.messymariage.crafting.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class RecipeList implements Listener {

    final private Plugin plugin;
    final Set<NamespacedKey> customRecipes = new HashSet<>();

    public RecipeList(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void addRecipe(NamespacedKey recipe) {
        customRecipes.add(recipe);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProposeToPlayer(final PlayerJoinEvent e) {
        Player joined = e.getPlayer();
        for(NamespacedKey r: customRecipes) {
            joined.discoverRecipe(r);
        }
    }
}
