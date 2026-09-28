package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerSpawnListener implements Listener {

    private final EventManager plugin;

    public PlayerSpawnListener(EventManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location spawnLoc = plugin.spawnCommand.loadSpawnLocation();
        if (spawnLoc == null) return;

        boolean ignoreBed = plugin.getConfig().getBoolean("ignore-bed-spawn", false);

        if (ignoreBed || !event.isBedSpawn()) {
            event.setRespawnLocation(spawnLoc);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            Location spawnLoc = plugin.spawnCommand.loadSpawnLocation();
            if (spawnLoc != null) {
                event.getPlayer().teleport(spawnLoc);
            }
        }

        if (plugin.getConfig().getString("join-message") != null || !plugin.getConfig().getString("join-message").isEmpty()) {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("join-message"));
            joinMessage = joinMessage.replace("%player%", event.getPlayer().getName());
            event.setJoinMessage(joinMessage);
        } else {
            event.setJoinMessage(null);
        }
    }
}