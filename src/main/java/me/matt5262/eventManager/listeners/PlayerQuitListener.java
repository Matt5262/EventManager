package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.EventManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    public static final Set<UUID> deathKickedPlayers = new HashSet<>();

    private final EventManager plugin;

    public PlayerQuitListener(EventManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (deathKickedPlayers.remove(player.getUniqueId())) {
            event.quitMessage(null);
        }

        if (plugin.getConfig().getString("quit-message") != null || !plugin.getConfig().getString("quit-message").isEmpty()) {
            String joinMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("quit-message"));
            joinMessage = joinMessage.replace("%player%", event.getPlayer().getName());
            event.setQuitMessage(joinMessage);
        } else {
            event.setQuitMessage(null);
        }
    }

}
