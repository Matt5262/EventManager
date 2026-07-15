package me.matt5262.eventManager.commands;

import me.matt5262.eventManager.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final EventManager plugin;

    public SpawnCommand(EventManager plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
            plugin.getServer().getLogger().warning("SpawnCommand has been called.");
        return true;
    }
}
