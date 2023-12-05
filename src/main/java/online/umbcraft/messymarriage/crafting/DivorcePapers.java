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

public class DivorcePapers {

    final private Plugin plugin;
    final private DiscoverRecipes recipes;

    public DivorcePapers(Plugin plugin, DiscoverRecipes recipes) {
        this.plugin = plugin;
        this.recipes = recipes;
    }

    public void start() {
        ItemStack ringBox = new ItemStack(Material.PAPER, 1);
        ItemMeta ringMeta = ringBox.getItemMeta();
        ringMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&cDivorce Papers"));
        ringMeta.setLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&7Use this item to file for divorce!"),
                ChatColor.translateAlternateColorCodes('&',"&7Be wary of the consequences...")
        ));

        ringBox.setItemMeta(ringMeta);
        CustomItemIdentifier.giveTag(plugin, ringBox, "divorce-papers");
        CustomItemIdentifier.giveTag(plugin, ringBox, "divorce-item");
        CustomItemIdentifier.makeCustom(plugin, ringBox);

        NamespacedKey key = new NamespacedKey(plugin, "divorce-papers-recipe");
        recipes.addRecipe(key);
        ShapedRecipe ringRecipe = new ShapedRecipe(key, ringBox);

        String[] shape = {
                " t ",
                "pPi",
                " a "};

        ringRecipe.shape(shape);

        ringRecipe.setIngredient('t', Material.TNT);
        ringRecipe.setIngredient('p', Material.POISONOUS_POTATO);
        ringRecipe.setIngredient('P', Material.PAPER);
        ringRecipe.setIngredient('i', Material.INK_SAC);
        ringRecipe.setIngredient('a', Material.IRON_AXE);

        plugin.getServer().addRecipe(ringRecipe);
    }
}
