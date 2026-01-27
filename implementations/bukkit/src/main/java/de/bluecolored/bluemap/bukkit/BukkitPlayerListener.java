package de.bluecolored.bluemap.bukkit;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class BukkitPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        BukkitPlayer player = new BukkitPlayer(evt.getPlayer().getUniqueId(), evt.getPlayer().getName());
        BukkitPlugin.getInstance().getOnlinePlayerMap().put(evt.getPlayer().getUniqueId(), player);
        BukkitPlugin.getInstance().getOnlinePlayerList().add(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent evt) {
        UUID playerUUID = evt.getPlayer().getUniqueId();
        BukkitPlugin.getInstance().getOnlinePlayerMap().remove(playerUUID);
        synchronized (BukkitPlugin.getInstance().getOnlinePlayerList()) {
            BukkitPlugin.getInstance().getOnlinePlayerList().removeIf(p -> p.getUuid().equals(playerUUID));
        }
    }
}