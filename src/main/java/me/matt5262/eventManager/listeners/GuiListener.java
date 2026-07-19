package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.EventManager;
import me.matt5262.eventManager.commands.SpawnCommand;
import me.matt5262.eventManager.invHolders.SpawnMenuHolder;
import me.matt5262.eventManager.utils.ItemUtil;
import org.bukkit.*;
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

    private String getFormattedCoords(Location loc) {
        if (plugin.getConfig().getBoolean("use-precise-coordinates", false)) {
            return String.format("%.2f, %.2f, %.2f", loc.getX(), loc.getY(), loc.getZ());
        }
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    private String getFormattedYaw(Location loc) {
        if (plugin.getConfig().getBoolean("use-precise-yaw", false)) {
            return String.format("%.2f°", loc.getYaw());
        }
        long snappedYaw = Math.round(loc.getYaw() / 90.0) * 90;
        if (snappedYaw == 270) snappedYaw = -90;
        if (snappedYaw == -270) snappedYaw = 90;
        if (snappedYaw == 360 || snappedYaw == -360) snappedYaw = 0;
        return snappedYaw + "°";
    }

    private String getFormattedPitch(Location loc) {
        if (plugin.getConfig().getBoolean("use-precise-pitch", false)) {
            return String.format("%.2f°", loc.getPitch());
        }
        return (Math.round(loc.getPitch() / 90.0) * 90) + "°";
    }

    public void openSetSpawnConf(Player player) {
        SpawnMenuHolder holder = new SpawnMenuHolder();
        Inventory inv = Bukkit.createInventory(holder, 27, ChatColor.translateAlternateColorCodes('&', "&eConfirm Set Spawn?"));
        holder.setInventory(inv);

        inv.setItem(12, ItemUtil.createGuiItem(
                plugin,
                Material.GREEN_STAINED_GLASS_PANE,
                "&aSet Spawn",
                "confirm_set_spawn"
        ));
        inv.setItem(14, ItemUtil.createGuiItem(
                plugin,
                Material.RED_STAINED_GLASS_PANE,
                "&cCancel",
                "cancel_set_spawn"
        ));
        inv.setItem(13, ItemUtil.createGuiItem(
                plugin,
                Material.GRASS_BLOCK,
                "&eConfirm Set Spawn",
                "visual_item",
                "&fSet the spawn point on:",
                "&fCoordinates: &6" + getFormattedCoords(player.getLocation()),
                "&fYaw: &6" + getFormattedYaw(player.getLocation()),
                "&fPitch: &6" + getFormattedPitch(player.getLocation()),
                "&fWait time: &6" + plugin.getConfig().getInt("wait-time", 5),
                "&fDelay: &6" + plugin.getConfig().getInt("delay", 15)
        ));
        inv.setItem(18, ItemUtil.createGuiItem(
                plugin,
                ItemUtil.getCustomHead(plugin, "http://textures.minecraft.net/texture/db4898c14428dd2e4011d9be3760ec6bab521ae5651f6e20ad5341a0f5afce28"),
                "&eEdit",
                "edit_set_spawn"
        ));

        player.openInventory(inv);
    }

    public void openEditSetSpawn(Player player) {
        SpawnMenuHolder holder = new SpawnMenuHolder();
        Inventory inv = Bukkit.createInventory(holder, 27, ChatColor.translateAlternateColorCodes('&', "&eSet Spawn Settings"));
        holder.setInventory(inv);

        boolean usePreciseCoords = plugin.getConfig().getBoolean("use-precise-coordinates", false);
        boolean usePreciseYaw = plugin.getConfig().getBoolean("use-precise-yaw", false);
        boolean usePrecisePitch = plugin.getConfig().getBoolean("use-precise-pitch", false);

        String coordColor = usePreciseCoords ? "&a" : "&c";
        String yawColor = usePreciseYaw ? "&a" : "&c";
        String pitchColor = usePrecisePitch ? "&a" : "&c";

        inv.setItem(11, ItemUtil.createGuiItem(
                plugin,
                Material.ARROW,
                coordColor + "Use Precise Coordinates",
                "toggle_use_precise_coordinates",
                "&fEnabling this will use decimals.",
                "&fThe following coordinates will be used:",
                "&6" + getFormattedCoords(player.getLocation())
        ));

        inv.setItem(12, ItemUtil.createGuiItem(
                plugin,
                Material.COMPASS,
                yawColor + "Use Precise Yaw",
                "toggle_use_precise_yaw",
                "&fEnabling this will allow you to use a yaw,",
                "&fthat is not 90, 180, 270 or 360 degrees.",
                "&fThe following yaw will be used:",
                "&6" + getFormattedYaw(player.getLocation())
        ));

        inv.setItem(13, ItemUtil.createGuiItem(
                plugin,
                ItemUtil.getCustomHead(plugin, "http://textures.minecraft.net/texture/1a03d1b41561e5c3577dd871cddb56aae5a2a23db349fa8f9f5b78d5f928191"),
                pitchColor + "Use Precise Pitch",
                "toggle_use_precise_pitch",
                "&fEnabling this will allow you to use a pitch,",
                "&fthat is not -90, 0 or 90 degrees.",
                "&fThe following pitch will be used:",
                "&6" + getFormattedPitch(player.getLocation())
        ));

        inv.setItem(14, ItemUtil.createGuiItem(
                plugin,
                Material.CLOCK,
                "&eSet Wait Time",
                "set_wait_time",
                "&fCurrent wait time: &6" + plugin.getConfig().getInt("wait-time") + "s."
        ));

        inv.setItem(15, ItemUtil.createGuiItem(
                plugin,
                Material.BONE_MEAL,
                "&eSet Delay",
                "set_delay_time",
                "&fCurrent delay time: &6" + plugin.getConfig().getInt("delay") + "s."
        ));

        inv.setItem(18, ItemUtil.createGuiItem(
                plugin,
                Material.BARRIER,
                "&cBack",
                "edit_set_spawn_back"
        ));

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (event.getView().getTopInventory().getHolder() instanceof SpawnMenuHolder) {
            event.setCancelled(true);
        }

        if (clickedInventory != null) {

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            ItemMeta meta = clickedItem.getItemMeta();
            NamespacedKey key = new NamespacedKey(plugin, ItemUtil.ACTION_KEY);

            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                Player player = (Player) event.getWhoClicked();

                String action = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                if (action == null) return;

                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                switch (action) {
                    case "set_spawn":
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            openSetSpawnConf(player);
                        });
                        break;
                    case "cancel_set_spawn":
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            new SpawnCommand(plugin).openSpawnMenu(player);
                        });
                        break;
                    case "edit_set_spawn":
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            openEditSetSpawn(player);
                        });
                        break;
                    case "edit_set_spawn_back":
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            openSetSpawnConf(player);
                        });
                        break;
                    case "toggle_use_precise_coordinates":
                        boolean coordState = plugin.getConfig().getBoolean("use-precise-coordinates", false);
                        plugin.getConfig().set("use-precise-coordinates", !coordState);
                        plugin.getConfig().options().copyDefaults(true);
                        plugin.saveConfig();
                        Bukkit.getScheduler().runTask(plugin, () -> openEditSetSpawn(player));
                        break;
                    case "toggle_use_precise_yaw":
                        boolean yawState = plugin.getConfig().getBoolean("use-precise-yaw", false);
                        plugin.getConfig().set("use-precise-yaw", !yawState);
                        plugin.getConfig().options().copyDefaults(true);
                        plugin.saveConfig();
                        Bukkit.getScheduler().runTask(plugin, () -> openEditSetSpawn(player));
                        break;
                    case "toggle_use_precise_pitch":
                        boolean pitchState = plugin.getConfig().getBoolean("use-precise-pitch", false);
                        plugin.getConfig().set("use-precise-pitch", !pitchState);
                        plugin.getConfig().options().copyDefaults(true);
                        plugin.saveConfig();
                        Bukkit.getScheduler().runTask(plugin, () -> openEditSetSpawn(player));
                        break;
                    case "set_wait_time":
                        player.closeInventory();
                        openSignInput(player, "wait-time");
                        break;
                    case "set_delay_time":
                        break;
                    default:
                        plugin.getLogger().warning("No GUI action defined for tag: " + action);
                        break;
                }
            }
        }

    }

    // 🟢 1. The Opening Trigger: Simply fires an outbound UI editor packet to the screen
    private void openSignInput(Player player, String targetConfigKey) {
        NamespacedKey identityKey = new NamespacedKey(plugin, "active_sign_target");
        player.getPersistentDataContainer().set(identityKey, PersistentDataType.STRING, targetConfigKey);

        // Construct an outbound Open Sign Editor packet pointed at a dummy coordinate (0, 0, 0)
        com.comphenix.protocol.events.PacketContainer openSignPacket =
                com.comphenix.protocol.ProtocolLibrary.getProtocolManager().createPacket(com.comphenix.protocol.PacketType.Play.Server.OPEN_SIGN_EDITOR);

        // Target block coordinate location index
        openSignPacket.getBlockPositionModifier().write(0, new com.comphenix.protocol.wrappers.BlockPosition(0, 0, 0));

        // Is front side of sign (true)
        openSignPacket.getBooleans().write(0, true);

        try {
            com.comphenix.protocol.ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSignPacket);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send virtual sign packet: " + e.getMessage());
        }
    }

    // 🟢 2. The Text Receiver: Safely called directly from the ProtocolLib async thread context
    public void handleVirtualSignInput(Player player, String[] lines) {
        NamespacedKey identityKey = new NamespacedKey(plugin, "active_sign_target");

        if (player.getPersistentDataContainer().has(identityKey, PersistentDataType.STRING)) {
            String targetConfigKey = player.getPersistentDataContainer().get(identityKey, PersistentDataType.STRING);
            player.getPersistentDataContainer().remove(identityKey); // Clean container state immediately

            // Minecraft reads the first line text at index 0
            String inputLine = lines[0];

            // Re-sync with main server thread context before updating configurations or opening new GUI containers
            org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
                if (inputLine == null || inputLine.trim().isEmpty()) {
                    player.sendMessage(org.bukkit.ChatColor.RED + "Action canceled: input line was empty.");
                    openEditSetSpawn(player);
                    return;
                }

                try {
                    int seconds = Integer.parseInt(inputLine.trim());
                    if (seconds < 0) {
                        player.sendMessage(org.bukkit.ChatColor.RED + "Number must be a positive integer.");
                    } else {
                        plugin.getConfig().set(targetConfigKey, seconds);
                        plugin.saveConfig();
                        player.sendMessage(org.bukkit.ChatColor.GREEN + "Configuration updated successfully!");
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(org.bukkit.ChatColor.RED + "Invalid input! Please enter a valid whole number.");
                }

                // Safely bring player back into settings menu view window
                openEditSetSpawn(player);
            });
        }
    }
}
