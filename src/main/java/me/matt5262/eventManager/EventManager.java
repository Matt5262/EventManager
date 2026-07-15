package me.matt5262.eventManager;

import me.matt5262.eventManager.commands.SpawnMenuCommand;
import me.matt5262.eventManager.listeners.GuiListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getCommand("spawn").setExecutor(new SpawnMenuCommand(this));
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);

    }
}
