package me.matt5262.eventManager.utils;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryHolder implements org.bukkit.inventory.InventoryHolder {

    private Inventory inv;

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inv;
    }
}
