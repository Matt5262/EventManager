package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnCommand;
import me.matt5262.eventManager.listeners.GuiListener;
import me.matt5262.eventManager.listeners.PlayerSpawnListener;
import me.matt5262.eventManager.utils.SpawnFileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    private GuiListener guiListener;
    private PlayerSpawnListener playerSpawnListener;
    private SpawnFileManager spawnFileManager;
    public SpawnCommand spawnCommand;

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

    }

    public SpawnFileManager getSpawnFileManager() {
        return spawnFileManager;
    }
}
