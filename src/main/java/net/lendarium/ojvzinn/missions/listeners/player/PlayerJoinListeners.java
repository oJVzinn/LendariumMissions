package net.lendarium.ojvzinn.missions.listeners.player;

import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListeners implements Listener {

    @EventHandler
    public void onPlayerPreLoginAsyncListeners(AsyncPlayerPreLoginEvent event) {
        String name = event.getName();
        Profile.createProfile(name);
    }

    @EventHandler
    public void onPlayerJoinListeners(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfile(player.getName());
        if (profile == null) {
            Profile.createProfile(player.getName());
        }
    }
}
