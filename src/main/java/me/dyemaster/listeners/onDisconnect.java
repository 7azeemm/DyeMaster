package me.dyemaster.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.listeners.onChatMessage.waitingForInput;

public class onDisconnect implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        if (e.getPlayer().hasMetadata("DyeManager")) {
            UUID id = e.getPlayer().getUniqueId();

            e.getPlayer().removeMetadata("DyeManager", getPlugin());
            waitingForInput.remove(id);
        }
    }
}
