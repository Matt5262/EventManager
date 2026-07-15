package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    @Override
    public void onDisable() {

    }
}
