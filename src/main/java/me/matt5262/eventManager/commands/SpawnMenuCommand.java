package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.invHolders.InventoryHolder;
import me.matt5262.eventManager.utils.ItemUtil;
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

    public void openCustomInventory(Player player){
        InventoryHolder holder = new InventoryHolder();
        Inventory inv = Bukkit.createInventory(holder, 27, "SpawnGUI");
        holder.setInventory(inv);

        inv.setItem(13, ItemUtil.createGuiItem(plugin, Material.GRASS_BLOCK, "&eSet Spawn Here", "set_spawn"));

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
