package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnCommand;
import me.matt5262.eventManager.listeners.GuiListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    private GuiListener guiListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        guiListener = new GuiListener(this);

        SpawnCommand spawnCommand = new SpawnCommand(this);
        this.getCommand("spawn").setExecutor(spawnCommand);
        this.getCommand("spawn").setTabCompleter(spawnCommand);
        getServer().getPluginManager().registerEvents(guiListener, this);

    }
}
