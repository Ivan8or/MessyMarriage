package online.umbcraft.messymarriage.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class CustomItemIdentifier {

    final static private String TAG_CUSTOM = "CUSTOM";

    public static ItemStack giveTag(Plugin plugin, ItemStack item, String tag) {
        NamespacedKey key = new NamespacedKey(plugin, tag);

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(key, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean hasTag(Plugin plugin, ItemStack item, String tag) {

        if(item.getType() == Material.AIR)
            return false;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(plugin, tag);
        return container.has(key, PersistentDataType.INTEGER);
    }

    public static ItemStack makeCustom(Plugin plugin, ItemStack item) {
        return giveTag(plugin, item, TAG_CUSTOM);
    }

    public static boolean isCustom(Plugin plugin, ItemStack item) {
        return hasTag(plugin, item, TAG_CUSTOM);
    }
}
