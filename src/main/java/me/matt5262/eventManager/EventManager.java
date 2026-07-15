package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnCommand;
import me.matt5262.eventManager.listeners.GuiListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        SpawnCommand spawnCommand = new SpawnCommand(this);
        this.getCommand("spawn").setExecutor(spawnCommand);
        this.getCommand("spawn").setTabCompleter(spawnCommand);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);

    }
}
