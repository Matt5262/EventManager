package me.matt5262.eventManager.utils;

import me.matt5262.eventManager.EventManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SpawnFileManager {

    private final EventManager plugin;
    private File spawnFile;
    private FileConfiguration spawnConfig;

    public SpawnFileManager(EventManager plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        spawnFile = new File(plugin.getDataFolder(), "spawn.yml");
        boolean freshFile = false;

        if (!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
                freshFile = true;
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create spawn.yml!", e);
            }
        }

        spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);

        if (freshFile) {
            populateDefaults();
        }
    }

    public void reload() {
        spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
    }

    public FileConfiguration getData() {
        if (spawnConfig == null) {
            reload();
        }
        return spawnConfig;
    }

    public void saveData() {
        try {
            spawnConfig.save(spawnFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save spawn.yml!", e);
        }
    }

    private void populateDefaults() {
        spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        spawnConfig.set("use-precise-coordinates", plugin.getConfig().getBoolean("use-precise-coordinates", false));
        spawnConfig.set("use-precise-yaw", plugin.getConfig().getBoolean("use-precise-yaw", false));
        spawnConfig.set("use-precise-pitch", plugin.getConfig().getBoolean("use-precise-pitch", false));
        spawnConfig.set("wait-time", plugin.getConfig().getInt("wait-time", 5));
        spawnConfig.set("delay", plugin.getConfig().getInt("delay", 15));
        saveData();
    }


}
