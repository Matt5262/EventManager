package me.matt5262.eventManager.utils;

import me.matt5262.eventManager.EventManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {

    public static final String ACTION_KEY = "gui_action";

    public static ItemStack createGuiItem(EventManager plugin, Material material, String displayName, String actionTag) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);

            NamespacedKey key = new NamespacedKey(plugin, ACTION_KEY);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, actionTag);

            item.setItemMeta(meta);
        }
        return item;
    }
}
