package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.utils.InventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) {
            return;
        }

        if (clickedInventory.getHolder() instanceof InventoryHolder) {
            event.setCancelled(true);
        }
    }

}
