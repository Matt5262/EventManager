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
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {

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
        String noPermMsg = plugin.getConfig().getString("no-permission-message");
        String invalidArgsMsg = plugin.getConfig().getString("invalid-arguments-error");

        if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandsender-error")));
                return true;
            }

            if (args.length == 0) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUnfortunately this feature has yet to be implemented."));
                return true;
            }

            if (args.length < 1) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidArgsMsg));
                return true;
            }

            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "editor":
                    if (!player.hasPermission("eventmanager.admin.spawn")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
                        return true;
                    }
                    openSpawnMenu(player);
                    break;
                default:
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', invalidArgsMsg));
                    break;
            }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("eventmanager.admin.spawn")) {
                suggestions.add("editor");
            }
            String currentInput = args[0].toLowerCase();
            suggestions.removeIf(suggestion -> !suggestion.startsWith(currentInput));
            return suggestions;
        }
        return new ArrayList<>();
    }
}
