package net.lendarium.ojvzinn.missions.listeners.player;

import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.missions.type.MissionsType;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerBuildListeners implements Listener {

    @EventHandler
    public void onPlayerBuildListeners(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getProfile(player.getName());
        if (profile != null) {
            List<Missions> getMissionsFromType = Missions.getMissionFromType(MissionsType.BUILD_BLOCKS).stream().filter(missions -> !profile.getCache().getManager().isCompleted(missions)).collect(Collectors.toList());
            for (Missions missions : getMissionsFromType) {
                if (profile.getCache().isSelectedMission(missions)) {
                    if (missions.getTypeValue() != null) {
                        Material material = Material.matchMaterial(missions.getTypeValue());
                        if (event.getBlock().getType().equals(material)) {
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
