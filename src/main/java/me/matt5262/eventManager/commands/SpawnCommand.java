package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.invHolders.SpawnMenuHolder;
import me.matt5262.eventManager.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final EventManager plugin;
    public SpawnCommand(EventManager plugin) {
        this.plugin = plugin;
    }

    public void openSpawnMenu(Player player){
        SpawnMenuHolder holder = new SpawnMenuHolder();
        Inventory inv = Bukkit.createInventory(holder, 27, ChatColor.translateAlternateColorCodes('&', "&eSpawn Editor"));
        holder.setInventory(inv);

        inv.setItem(13, ItemUtil.createGuiItem(
                plugin,
                Material.GRASS_BLOCK,
                "&aSet Spawn",
                "set_spawn",
                "&eSet the spawn point for &6" + player.getLocation().getWorld().getName() + "&6!"));

        player.openInventory(inv);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage("&cOnly players can execute this command!");
                return true;
            }

            if (args.length == 0) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUnfortunately this feature has yet to be implemented."));
                return true;
            }

            if (args.length < 1) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid argument(s)!"));
                return true;
            }

            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "editor":
                    openSpawnMenu(player);
                    break;
                default:
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid argument(s)!"));
                    break;
            }

        return true;
    }
}
