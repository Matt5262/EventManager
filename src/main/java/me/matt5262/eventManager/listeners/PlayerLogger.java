package me.matt5262.eventManager.listeners;

import me.matt5262.eventManager.utils.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.time.format.DateTimeFormatter;

public class PlayerLogger implements Listener {

    // Reference to the LogManager class
    private final LogManager logManager;
    // DateTimeFormatter to format the date and time in the log file
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // When EventListener is created, it will tell this plugin where the LogManager is
    public PlayerLogger(LogManager logManager) {
        this.logManager = logManager;
    }

    // When a player dies, this method will be called and log the death or kill in the log file
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Get the player who died and the player who killed them
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Get the current date and time in the format "yyyy-MM-dd HH:mm:ss" from the timeFormatter
        String timestamp = java.time.LocalDateTime.now().format(timeFormatter);

        // If the player was killed by another player, log the kill in the log file
        if (killer != null) {
            String path = "kills." + System.currentTimeMillis();
            // logManager.getLogConfig() runs the method inside logManager to get the logConfig object,
            // which is a FileConfiguration object that represents the log file.
            // Then, it sets the values for the time, killer, victim, and weapon in the log file
            // using the set() method of the FileConfiguration object.
            logManager.getLogConfig().set(path + ".time", timestamp);
            logManager.getLogConfig().set(path + ".killer", killer.getName());
            logManager.getLogConfig().set(path + ".victim", victim.getName());
            logManager.getLogConfig().set(path + ".weapon", killer.getInventory().getItemInMainHand().getType().toString());
        } else {
            // If the player was not killed by another player, log the death in the log file
            String path = "deaths." + System.currentTimeMillis();
            logManager.getLogConfig().set(path + ".time", timestamp);
            logManager.getLogConfig().set(path + ".victim", victim.getName());
            logManager.getLogConfig().set(path + ".cause",
                    victim.getLastDamageCause() != null ? victim.getLastDamageCause().getCause().toString() : "UNKNOWN");
        }
        // Save the log file after logging the death or kill
        logManager.saveLog();
    }

    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        // Get the advancement key and check if it starts with "recipes/"
        String key = event.getAdvancement().getKey().toString();
        if (key.startsWith("minecraft:recipes/")) {
            return;
        }

        Player player = event.getPlayer();
        String timestamp = java.time.LocalDateTime.now().format(timeFormatter);

        String path = "advancements." + System.currentTimeMillis();
        logManager.getLogConfig().set(path + ".time", timestamp);
        logManager.getLogConfig().set(path + ".player", player.getName());
        logManager.getLogConfig().set(path + ".advancement", key);

        logManager.saveLog();
    }
}
