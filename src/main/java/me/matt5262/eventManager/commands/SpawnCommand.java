package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.utils.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final EventManager plugin;

    InventoryHolder holder = new InventoryHolder();

    public SpawnCommand(EventManager plugin) {
        this.plugin = plugin;
    }

    public void openCustomInventory(Player player){
        Inventory inv = Bukkit.createInventory(holder, 27, "SpawnGUI");

        ItemStack grassItem = new ItemStack(Material.GRASS_BLOCK);

        holder.setInventory(inv);

        inv.setItem(13, grassItem);
        player.openInventory(inv);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
            plugin.getServer().getLogger().warning("SpawnCommand has been called.");
            commandSender.sendMessage("SpawnCommand has been called.");

            if (commandSender instanceof Player player) {
                openCustomInventory(player);
            }
        return true;
    }
}
