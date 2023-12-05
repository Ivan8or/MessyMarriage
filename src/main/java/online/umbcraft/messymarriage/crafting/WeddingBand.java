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

public class WeddingBand {

    final private Plugin plugin;
    final private DiscoverRecipes recipes;

    public WeddingBand(Plugin plugin, DiscoverRecipes recipes) {
        this.plugin = plugin;
        this.recipes = recipes;
    }

    public void start() {
        ItemStack ringBox = new ItemStack(Material.DARK_OAK_BUTTON, 1);
        ItemMeta ringMeta = ringBox.getItemMeta();
        ringMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&eWedding Band"));
        ringMeta.setLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&7Use this band to propose to your true love!"),
                ChatColor.translateAlternateColorCodes('&',"&7You must have an amiability of &650&7 to propose.")
        ));

        ringBox.setItemMeta(ringMeta);
        CustomItemIdentifier.giveTag(plugin, ringBox, "wedding-band");
        CustomItemIdentifier.giveTag(plugin, ringBox, "wedding-item");
        CustomItemIdentifier.makeCustom(plugin, ringBox);

        NamespacedKey key = new NamespacedKey(plugin, "wedding-band-recipe");
        recipes.addRecipe(key);
        ShapedRecipe ringRecipe = new ShapedRecipe(key, ringBox);

        String[] shape = {
                "xxx",
                "x x",
                "xxx"};

        ringRecipe.shape(shape);

        ringRecipe.setIngredient('x', Material.GOLD_INGOT);
        plugin.getServer().addRecipe(ringRecipe);
    }

}
