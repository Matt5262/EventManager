package me.matt5262.eventManager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
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

        // ProtocolLib
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                com.comphenix.protocol.wrappers.BlockPosition pos = packet.getBlockPositionModifier().read(0);
                if (pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0) {
                    String[] lines = packet.getStringArrays().read(0);
                    guiListener.handleVirtualSignInput(event.getPlayer(), lines);
                }


            }
        });

    }
}
