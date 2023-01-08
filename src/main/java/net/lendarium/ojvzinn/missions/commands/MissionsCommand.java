package net.lendarium.ojvzinn.missions.commands;
import net.lendarium.ojvzinn.missions.menu.MenuPrincipalMissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissionsCommand extends Commands {

    protected MissionsCommand() {
        super("missions", "missoes", "n");
    }

    @Override
    void perfomace(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando é exclusivo para jogadores.");
            return;
        }

        Player player = (Player) sender;
        new MenuPrincipalMissions(player);
    }
}
