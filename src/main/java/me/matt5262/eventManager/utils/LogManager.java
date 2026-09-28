package me.matt5262.eventManager.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LogManager {

    // Reference to the main plugin class
    private final JavaPlugin plugin;
    // Log file
    private File logFile;
    // logFile but readable as a FileConfiguration object
    private FileConfiguration logConfig;

    // When LogManager is created, it will tell this plugin where the main plugin is and call the createLogFile() method
    public LogManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createLogFile();
    }

    // Create the log file if it doesn't exist and load it as a FileConfiguration object
    private void createLogFile() {
        // Make the plugin's data directory if it doesn't exist
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Create the log file in the plugin's data directory
        logFile = new File(plugin.getDataFolder(), "player_logs.yml");

        // If the log file doesn't exist, create it and log an error if it fails
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create player_logs.yml file!");
                e.printStackTrace();
            }
        }

        // Load the log file as a FileConfiguration object
        logConfig = YamlConfiguration.loadConfiguration(logFile);
    }

    // Hands over the logConfig object
    public FileConfiguration getLogConfig() {
        return logConfig;
    }

    // Saves the logConfig object to the logFile
    public void saveLog() {
        try {
            logConfig.save(logFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save player_logs.yml file!");
            e.printStackTrace();
        }
    }
}
