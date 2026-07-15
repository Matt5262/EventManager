package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.invHolders.InventoryHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuiListener implements Listener {

    private final EventManager plugin;

    public GuiListener(EventManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof InventoryHolder) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            ItemMeta meta = clickedItem.getItemMeta();

            if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, "special_gui_grassblock"), PersistentDataType.STRING)) {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("§aYou clicked the SPECIFIC sacred dirt block!");
                player.closeInventory();
            }

        }

    }

}
