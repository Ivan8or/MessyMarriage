package online.umbcraft.messymariage.crafting;

import online.umbcraft.messymariage.util.CustomItemIdentifier;
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

    public DivorcePapers(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        ItemStack ringBox = new ItemStack(Material.PAPER, 1);
        ItemMeta ringMeta = ringBox.getItemMeta();
        ringMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&cDivorce Papers"));
        ringMeta.setLore(List.of(
                ChatColor.translateAlternateColorCodes('&',"&7Use this band to file for divorce!"),
                ChatColor.translateAlternateColorCodes('&',"&7Be wary of the consequences...")
        ));

        ringBox.setItemMeta(ringMeta);
        CustomItemIdentifier.giveTag(plugin, ringBox, "divorce-papers");
        CustomItemIdentifier.giveTag(plugin, ringBox, "divorce-item");
        CustomItemIdentifier.makeCustom(plugin, ringBox);

        ShapedRecipe ringRecipe = new ShapedRecipe(new NamespacedKey(plugin, "divorce-papers-recipe"), ringBox);

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
