package me.matt5262.eventManager.utils;

import me.matt5262.eventManager.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    public static final String ACTION_KEY = "gui_action";

    public static ItemStack createGuiItem(EventManager plugin, Material material, String displayName, String actionTag) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

            NamespacedKey key = new NamespacedKey(plugin, ACTION_KEY);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, actionTag);

            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createGuiItem(EventManager plugin, Material material, String displayName, String actionTag, String... lore) {
        ItemStack item = createGuiItem(plugin, material, displayName, actionTag);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            List<String> formattedLore = new ArrayList<>();
            for (String line : lore) {
                formattedLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }

            meta.setLore(formattedLore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
