package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.EventManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final EventManager plugin;

    public DeathListener(EventManager eventManager) {
        this.plugin = eventManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();
        Location deathLocation = player.getLocation();
        String deathMessage = plugin.getConfig().getString("deathban-announcement");
        String banMessage = plugin.getConfig().getString("deathban-message");
        String kickMessage = plugin.getConfig().getString("deathban-kick-message");

        if (!player.hasPermission("eventmanager.deathban.bypass")) {
            // Death effects
            if (plugin.getConfig().getBoolean("custom-death-effects")) {
                if (deathLocation != null && deathLocation.getWorld() != null) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        deathLocation.getWorld().strikeLightningEffect(deathLocation);
                    }, 1L);

                    double maxHearDistanceSquared = 64 * 64;

                    for (Player p : deathLocation.getWorld().getPlayers()) {
                        if (p.getLocation().distanceSquared(deathLocation) > maxHearDistanceSquared) {
                            p.playSound(
                                    p.getLocation(),
                                    Sound.ENTITY_LIGHTNING_BOLT_THUNDER,
                                    SoundCategory.WEATHER,
                                    10f,
                                    0.8f);
                        }
                    }

                    if (!deathMessage.isEmpty() || deathMessage != null) {
                        event.deathMessage(null);
                        deathMessage = deathMessage.replace("%player%", player.getName());
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', deathMessage));
                    }

                    PlayerQuitListener.deathKickedPlayers.add(player.getUniqueId());

                    player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
                    Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), banMessage, null, "EventManager");
                }
            }
        }
    }
}
