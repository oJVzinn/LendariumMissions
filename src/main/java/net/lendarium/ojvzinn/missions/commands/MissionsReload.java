package net.lendarium.ojvzinn.missions.commands;

import net.lendarium.ojvzinn.missions.missions.object.Missions;
import org.bukkit.command.CommandSender;

public class MissionsReload extends Commands {

    protected MissionsReload() {
        super("missionsreload");
    }

    @Override
    void perfomace(CommandSender sender, String s, String[] args) {
        Missions.reload();
        sender.sendMessage("§aTodas as missões foram recarregadas com sucesso!");
    }
}
