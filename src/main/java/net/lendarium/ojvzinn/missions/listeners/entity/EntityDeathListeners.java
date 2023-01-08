package net.lendarium.ojvzinn.missions.listeners.entity;

import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.missions.type.MissionsType;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EntityDeathListeners implements Listener {

    @EventHandler
    public void onEntityDeathListeners(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        Entity entity = event.getEntity();
        if (player != null) {
            Profile profile = Profile.getProfile(player.getName());
            if (profile != null) {
                List<Missions> getMissionsFromType = Missions.getMissionFromType(MissionsType.KILL_MOBS).stream().filter(missions -> !profile.getCache().getManager().isCompleted(missions)).collect(Collectors.toList());
                ;
                for (Missions missions : getMissionsFromType) {
                    if (missions.getTypeValue() != null) {
                        EntityType type = EntityType.valueOf(missions.getTypeValue());
                        if (entity.getType().equals(type)) {
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
