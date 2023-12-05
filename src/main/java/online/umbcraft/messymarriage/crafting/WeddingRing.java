package online.umbcraft.messymarriage.crafting;

import online.umbcraft.messymarriage.crafting.listeners.DiscoverRecipes;
import online.umbcraft.messymarriage.util.CustomItemIdentifier;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class WeddingRing {

    final private Plugin plugin;
    final private DiscoverRecipes recipes;

    public WeddingRing(Plugin plugin, DiscoverRecipes recipes) {
        this.plugin = plugin;
        this.recipes = recipes;
    }

    public void start() {
        ItemStack ringBox = new ItemStack(Material.POLISHED_BLACKSTONE_BUTTON, 1);
        ItemMeta ringMeta = ringBox.getItemMeta();
        ringMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&bWedding Ring"));
        ringMeta.setLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&7Use this ring to propose to your true love!"),
                ChatColor.translateAlternateColorCodes('&',"&7You must have an amiability of &650&7 to propose.")
        ));

        ringBox.setItemMeta(ringMeta);
        CustomItemIdentifier.giveTag(plugin, ringBox, "wedding-ring");
        CustomItemIdentifier.giveTag(plugin, ringBox, "wedding-item");
        CustomItemIdentifier.makeCustom(plugin, ringBox);

        NamespacedKey key = new NamespacedKey(plugin, "wedding-ring-recipe");
        recipes.addRecipe(key);
        ShapedRecipe ringRecipe = new ShapedRecipe(key, ringBox);

        String[] shape = {
                "xDx",
                "xgx",
                "xxx"};

        ringRecipe.shape(shape);

        ringRecipe.setIngredient('x', Material.OBSIDIAN);
        ringRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        ringRecipe.setIngredient('g', Material.GOLD_INGOT);

        plugin.getServer().addRecipe(ringRecipe);
    }

}
