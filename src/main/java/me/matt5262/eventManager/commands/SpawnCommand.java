package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.invHolders.SpawnMenuHolder;
import me.matt5262.eventManager.utils.ItemUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
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

    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message))
        );
    }

    private void playConfigSound(Player player, String configKey, String defaultSound, float volume, float pitch) {
        String soundName = plugin.getConfig().getString(configKey, defaultSound);

        if (soundName != null && !soundName.isBlank()) {
            player.playSound(player.getLocation(), soundName.trim().toLowerCase(Locale.ROOT), volume, pitch);
        } else {
            plugin.getLogger().warning("Invalid sound specified in config for key '" + configKey + "': " + soundName);
        }
    }

    private void handleSpawnTeleport(Player player) {
        Location spawnLoc = loadSpawnLocation();
        if (spawnLoc == null) {
            String msg = plugin.getConfig().getString("spawn-not-set-message", "&cSpawn has not been set yet!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return;
        }

        UUID uuid = player.getUniqueId();
        FileConfiguration spawnData = plugin.getSpawnFileManager().getData();

        // 1. Check Cooldown
        int delaySeconds = spawnData.getInt("delay", 15);
        if (cooldowns.containsKey(uuid)) {
            long secondsLeft = (cooldowns.get(uuid) - System.currentTimeMillis()) / 1000;
            if (secondsLeft > 0) {
                String msg = plugin.getConfig().getString("spawn-cooldown-message", "&cYou must wait %seconds% second(s) before using /spawn again.");
                msg = msg.replace("%seconds%", String.valueOf(secondsLeft));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                return;
            }
        }

        // 2. Warmup Execution
        int waitTimeSeconds = spawnData.getInt("wait-time", 5);

        if (pendingTeleports.containsKey(uuid)) {
            pendingTeleports.get(uuid).cancel();
            pendingTeleports.remove(uuid);
        }

        if (waitTimeSeconds <= 0) {
            executeTeleport(player, spawnLoc, delaySeconds);
            return;
        }

        Location startLoc = player.getLocation().clone();
        double threshold = plugin.getConfig().getDouble("movement-threshold-blocks", 1.0);
        double thresholdSquared = threshold * threshold;

        BukkitTask task = new BukkitRunnable() {
            int ticksElapsed = 0;
            int secondsRemaining = waitTimeSeconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    pendingTeleports.remove(uuid);
                    cancel();
                    return;
                }

                // Movement Check (> 1 block away from initial point)
                if (!player.getWorld().equals(startLoc.getWorld()) ||
                        player.getLocation().distanceSquared(startLoc) > thresholdSquared) {

                    String cancelMsg = plugin.getConfig().getString("spawn-moved-cancelled-message", "&cTeleportation cancelled because you moved!");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cancelMsg));
                    pendingTeleports.remove(uuid);
                    cancel();
                    return;
                }

                // Send action bar prompt continuously every tick
                String actionBarTemplate = plugin.getConfig().getString("spawn-warmup-actionbar", "&fTeleporting in &b%seconds%&f seconds... Do not move!");
                sendActionBar(player, actionBarTemplate.replace("%seconds%", String.valueOf(secondsRemaining)));

                // Trigger tick sound and update timer every 20 ticks (1 second)
                if (ticksElapsed % 20 == 0) {
                    playConfigSound(player, "teleport-tick-sound", "block.note_block.hat", 1.0f, 1.0f);

                    if (secondsRemaining <= 0) {
                        executeTeleport(player, spawnLoc, delaySeconds);
                        pendingTeleports.remove(uuid);
                        cancel();
                        return;
                    }
                    secondsRemaining--;
                }

                ticksElapsed++;
            }
        }.runTaskTimer(plugin, 0L, 1L);

        pendingTeleports.put(uuid, task);
    }

    private void executeTeleport(Player player, Location targetLoc, int delaySeconds) {
        player.teleport(targetLoc);

        String successMsg = plugin.getConfig().getString("spawn-teleport-success-message", "&aTeleported to spawn!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', successMsg));

        playConfigSound(player, "teleport-success-sound", "ENTITY_ENDERMAN_TELEPORT", 1.0f, 1.0f);

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
