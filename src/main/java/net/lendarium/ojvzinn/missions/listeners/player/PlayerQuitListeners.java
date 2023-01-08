package net.lendarium.ojvzinn.missions.listeners.player;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerQuitListeners implements Listener {

    @EventHandler
    public void onPlayerQuitListeners(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                Profile.destroyProfile(player.getName());
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 0L);
    }
}
