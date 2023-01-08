package net.lendarium.ojvzinn.missions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public abstract class Commands extends Command {

    protected Commands(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));

        try {
            SimpleCommandMap commandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            commandMap.register("LendariumMissions", this);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        perfomace(commandSender, s, strings);
        return true;
    }

    abstract void perfomace(CommandSender sender, String s, String[] args);

    public static void setupCommands() {
        new MissionsCommand();
        new MissionsReload();
    }
}
