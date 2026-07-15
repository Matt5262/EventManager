package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.invHolders.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SpawnMenuCommand implements CommandExecutor {

    private final EventManager plugin;
    public SpawnMenuCommand(EventManager plugin) {
        this.plugin = plugin;
    }

    InventoryHolder holder = new InventoryHolder();

    public void openCustomInventory(Player player){
        Inventory inv = Bukkit.createInventory(holder, 27, "SpawnGUI");

        holder.setInventory(inv);
        ItemStack setSpawnItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta setSpawnItemMeta = setSpawnItem.getItemMeta();

        if (setSpawnItemMeta != null) {
            setSpawnItemMeta.setDisplayName("Set Spawn");
            NamespacedKey key = new NamespacedKey(plugin, "special_gui_grassblock");
            setSpawnItemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "is_special");
            setSpawnItem.setItemMeta(setSpawnItemMeta);
        }

        inv.setItem(13, setSpawnItem);
        player.openInventory(inv);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {

            if (commandSender instanceof Player player) {
                openCustomInventory(player);
            }

        return true;
    }
}
