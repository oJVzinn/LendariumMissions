package net.lendarium.ojvzinn.missions.listeners;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.listeners.entity.EntityDeathListeners;
import net.lendarium.ojvzinn.missions.listeners.player.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Listeners {


    public static void setupListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerMoveListeners(), Main.getInstance());
        pm.registerEvents(new PlayerJoinListeners(), Main.getInstance());
        pm.registerEvents(new PlayerQuitListeners(), Main.getInstance());
        pm.registerEvents(new PlayerBreakListeners(), Main.getInstance());
        pm.registerEvents(new EntityDeathListeners(), Main.getInstance());
        pm.registerEvents(new PlayerBuildListeners(), Main.getInstance());
        pm.registerEvents(new PlayerSellListeners(), Main.getInstance());
    }
}
