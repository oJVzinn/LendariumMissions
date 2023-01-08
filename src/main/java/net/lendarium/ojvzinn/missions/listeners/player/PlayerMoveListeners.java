package net.lendarium.ojvzinn.missions.listeners.player;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.missions.type.MissionsType;
import net.lendarium.ojvzinn.missions.player.Profile;
import net.lendarium.ojvzinn.missions.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerMoveListeners implements Listener {

    @EventHandler
    public void onPlayerMoveListeners(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfile(player.getName());
        if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
            if (profile != null) {
                List<Missions> getMissionsFromType = Missions.getMissionFromType(MissionsType.MOVE_BLOCKS).stream().filter(missions -> !profile.getCache().getManager().isCompleted(missions)).collect(Collectors.toList());;
                for (Missions missions : getMissionsFromType) {
                    if (profile.getCache().isSelectedMission(missions)) {
                        if (missions.getTypeValue() != null) {
                            Material material = Material.matchMaterial(missions.getTypeValue());
                            if (player.getLocation().getBlock().getType().equals(material)) {
                                int currentProgress = profile.getCache().getProgress(missions).getValue();
                                currentProgress++;
                                if (currentProgress == missions.getValueToCompleted()) {
                                    profile.getCache().getManager().setCompleted(missions);
                                    return;
                                }
                                profile.getCache().getManager().addProgressToMission(missions, currentProgress);
                            }
                        } else {
                            int currentProgress = profile.getCache().getProgress(missions).getValue();
                            currentProgress++;
                            if (currentProgress == missions.getValueToCompleted()) {
                                profile.getCache().getManager().setCompleted(missions);
                                return;
                            }
                            if (currentProgress == missions.getValueToCompleted()) {
                                return;
                            }
                            profile.getCache().getManager().addProgressToMission(missions, currentProgress);
                        }
                    }
                }
            }
        }
    }
}
