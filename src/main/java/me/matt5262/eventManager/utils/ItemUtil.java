package me.matt5262.eventManager.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.matt5262.eventManager.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

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

    public static ItemStack getCustomHead(EventManager plugin, String textureInput) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if (meta != null) {
            try {
                PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
                PlayerTextures textures = profile.getTextures();

                URL url;
                if (textureInput.startsWith("http")) {
                    url = new URL(textureInput);
                }else {
                    byte[] decoded = Base64.getDecoder().decode(textureInput);
                    String json = new String(decoded);
                    String urlString = json.substring(json.indexOf("http"), json.indexOf("\"", json.indexOf("http")));
                    url = new URL(urlString);
                }

                textures.setSkin(url);
                profile.setTextures(textures);
                meta.setOwnerProfile(profile);
                item.setItemMeta(meta);
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to apply custom head texture " + e.getMessage());
            }
        }
        return item;
    }

}
