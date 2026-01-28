package de.bluecolored.bluemap.bukkit;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventPriority;
import de.bluecolored.bluemap.common.plugin.skins.MojangSkinProvider;

import java.util.UUID;

public class BukkitPlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        MojangSkinProvider mojangSkinProvider = new MojangSkinProvider();
        UUID playerUUID = mojangSkinProvider.getUUID(evt.getPlayer().getName());

        BukkitPlayer player = new BukkitPlayer(playerUUID, evt.getPlayer().getName());
        BukkitPlugin.getInstance().getOnlinePlayerMap().put(playerUUID, player);
        BukkitPlugin.getInstance().getOnlinePlayerList().add(player);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent evt) {
        MojangSkinProvider mojangSkinProvider = new MojangSkinProvider();

        UUID playerUUID = mojangSkinProvider.getUUID(evt.getPlayer().getName());
        BukkitPlugin.getInstance().getOnlinePlayerMap().remove(playerUUID);
        synchronized (BukkitPlugin.getInstance().getOnlinePlayerList()) {
            BukkitPlugin.getInstance().getOnlinePlayerList().removeIf(p -> p.getUuid().equals(playerUUID));
        }
    }
}