package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnCommand;
import me.matt5262.eventManager.listeners.*;
import me.matt5262.eventManager.utils.LogManager;
import me.matt5262.eventManager.utils.SpawnFileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    private GuiListener guiListener;
    private PlayerSpawnListener playerSpawnListener;
    private SpawnFileManager spawnFileManager;
    public SpawnCommand spawnCommand;
    // Reference to the LogManager class
    private LogManager logManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.spawnFileManager = new SpawnFileManager(this);
        spawnFileManager.setup();

        guiListener = new GuiListener(this);
        playerSpawnListener = new PlayerSpawnListener(this);

        this.spawnCommand = new SpawnCommand(this);
        this.getCommand("spawn").setExecutor(spawnCommand);
        this.getCommand("spawn").setTabCompleter(spawnCommand);
        getServer().getPluginManager().registerEvents(guiListener, this);
        getServer().getPluginManager().registerEvents(playerSpawnListener, this);

        // When the plugin is enabled, it will create a new LogManager object and pass this plugin to it
        this.logManager = new LogManager(this);
        // Register the EventListener class to listen for events and pass the logManager to it and pass this plugin to EventListener
        getServer().getPluginManager().registerEvents(new PlayerLogger(logManager), this);

        Bukkit.getConsoleSender().sendMessage("[EventManager] "+ ChatColor.GREEN + "Logger enabled and logging events!");

        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    public SpawnFileManager getSpawnFileManager() {
        return spawnFileManager;
    }
}
