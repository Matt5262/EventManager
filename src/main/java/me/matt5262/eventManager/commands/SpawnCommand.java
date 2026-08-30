package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.invHolders.SpawnMenuHolder;
import me.matt5262.eventManager.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {

    private final EventManager plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, BukkitTask> pendingTeleports = new HashMap<>();

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
                "&fSet the spawn point for the /spawn command!"));

        player.openInventory(inv);
    }

    private Location loadSpawnLocation() {
        FileConfiguration config = plugin.getSpawnFileManager().getData();
        if (!config.contains("spawn.world")) return null;

        String worldName = config.getString("spawn.world");
        if (worldName == null || Bukkit.getWorld(worldName) == null) return null;

        return new Location(
                Bukkit.getWorld(worldName),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
    }

    private void handleSpawnTeleport(Player player) {
        Location spawnLoc = loadSpawnLocation();
        if (spawnLoc == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSpawn has not been set yet!"));
            return;
        }

        UUID uuid = player.getUniqueId();
        FileConfiguration data = plugin.getSpawnFileManager().getData();

        // 1. Check Cooldown
        int delaySeconds = data.getInt("delay", 15);
        if (cooldowns.containsKey(uuid)) {
            long secondsLeft = (cooldowns.get(uuid) - System.currentTimeMillis()) / 1000;
            if (secondsLeft > 0) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&cYou must wait " + secondsLeft + " second(s) before using /spawn again."));
                return;
            }
        }

        // 2. Warmup Execution
        int waitTimeSeconds = data.getInt("wait-time", 5);

        if (pendingTeleports.containsKey(uuid)) {
            pendingTeleports.get(uuid).cancel();
            pendingTeleports.remove(uuid);
        }

        if (waitTimeSeconds <= 0) {
            executeTeleport(player, spawnLoc, delaySeconds);
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&eTeleporting in " + waitTimeSeconds + " seconds... Do not move!"));

        Location startLoc = player.getLocation().clone();

        BukkitTask task = new BukkitRunnable() {
            int secondsRemaining = waitTimeSeconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    pendingTeleports.remove(uuid);
                    cancel();
                    return;
                }

                // Movement check (block coordinates only; allows head rotation)
                Location currentLoc = player.getLocation();
                if (currentLoc.getBlockX() != startLoc.getBlockX() ||
                        currentLoc.getBlockY() != startLoc.getBlockY() ||
                        currentLoc.getBlockZ() != startLoc.getBlockZ()) {

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTeleportation cancelled because you moved!"));
                    pendingTeleports.remove(uuid);
                    cancel();
                    return;
                }

                secondsRemaining--;

                if (secondsRemaining <= 0) {
                    executeTeleport(player, spawnLoc, delaySeconds);
                    pendingTeleports.remove(uuid);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

        pendingTeleports.put(uuid, task);
    }

    private void executeTeleport(Player player, Location targetLoc, int delaySeconds) {
        player.teleport(targetLoc);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleported to spawn!"));

        if (delaySeconds > 0) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (delaySeconds * 1000L));
        }
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
                handleSpawnTeleport(player);
                return true;
            }

            if (args.length > 1) {
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
