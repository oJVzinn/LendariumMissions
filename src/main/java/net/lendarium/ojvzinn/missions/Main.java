package net.lendarium.ojvzinn.missions;

import net.lendarium.ojvzinn.missions.commands.Commands;
import net.lendarium.ojvzinn.missions.database.DataBase;
import net.lendarium.ojvzinn.missions.database.DataTypes;
import net.lendarium.ojvzinn.missions.listeners.Listeners;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    public static Main getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        plugin = this;
    }

    @Override
    public void onEnable() {
        DataBase.setupDataBases(DataTypes.SQLITE, Main.getInstance());
        Listeners.setupListeners();
        Missions.setupMissions();
        Commands.setupCommands();

        sendMessage("O plugin ligou com sucesso!");
    }

    @Override
    public void onDisable() {
        Profile.loadProfiles().values().forEach(Profile::saveSync);
        sendMessage("O plugin desligou com sucesso!");
    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage("§a[" + getDescription().getName() + "] " + message);
    }
    public void sendMessage(String message, String color) {
        Bukkit.getConsoleSender().sendMessage("§" + color + "[" + getDescription().getName() + "] " + message);
    }
}
