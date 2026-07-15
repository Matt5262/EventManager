package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnCommand;
import me.matt5262.eventManager.listeners.GuiListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getCommand("spawn").setExecutor(new SpawnCommand(this));
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
