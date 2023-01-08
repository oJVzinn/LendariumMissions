package net.lendarium.ojvzinn.missions.missions;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.nms.NMS_1_8_R3;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class MissionsManager {

    private final Profile profile;

    public MissionsManager(Profile profile) {
        this.profile = profile;
    }

    public boolean isCompleted(Missions type) {
        return (boolean) profile.getCache().getMissionsCompleted(true).get(type.getId());
    }

    public void setCompleted(Missions type) {
        profile.getCache().getMissionsCompleted(true).remove(type.getId());
        profile.getCache().getMissionsCompleted(true).put(type.getId(), true);
        profile.getCache().removeSelectedMission(type);
        type.setupCommand(profile.getName());
        if (Missions.getMissionsCache().stream().filter(missions -> profile.getCache().getManager().isCompleted(missions)).count() == Missions.getMissionsCache().size()) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    if (i == 9) {
                        super.cancel();
                        this.cancel();
                        return;
                    }
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        NMS_1_8_R3.sendTitle(online, "§a§lDIGITEM, GG!", "§fO jogador §e" + profile.getName() + "§f acabou de finalizar TODAS as missões");
                    }
                    profile.getPlayer().getWorld().spawn(profile.getPlayer().getLocation(), Firework.class);
                    i++;
                }
            }.runTaskTimer(Main.getInstance(), 0L, 20);
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1.0F, 1.0F));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "darvip " + profile.getName() + " MVP 30");
            return;
        }
        long restantes = profile.getCache().getMissionsCompleted(true).values().stream().filter(o -> o.equals(false)).count();
        NMS_1_8_R3.sendTitle(profile.getPlayer(), "§a§lMISSÃO CONCLUIDA".toUpperCase(Locale.ROOT), "§fFalta completar mais §b§n" + restantes + "§f missões do servidor");
        NMS_1_8_R3.sendActionBar(profile.getPlayer(), "§eParabéns por completar a missão de §f" + type.getName() + "§e, recompensas recebidas ❤");
        if (profile.getCache().getAutoMission()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<Missions> missions = Missions.getMissionsCache().stream().filter(missions1 -> !profile.getCache().getManager().isCompleted(missions1)).collect(Collectors.toList());
                    if (!missions.isEmpty()) {
                        profile.getCache().getManager().setCompleted(missions.get(ThreadLocalRandom.current().nextInt(0,missions.size() - 1)));
                        NMS_1_8_R3.sendActionBar(getProfile().getPlayer(), "§eFoi selecionado uma nova missão para você automaticamente");
                    }
                }
            }.runTaskLater(Main.getInstance(), 20 * 10);
        }
        if (Main.getInstance().getConfig().getBoolean("missions.message.sound.use")) {
            String sound = Main.getInstance().getConfig().getString("missions.message.sound.sound");
            profile.getPlayer().playSound(profile.getPlayer().getLocation(), Sound.valueOf(sound.split(":")[0]), Float.parseFloat(sound.split(":")[1].split(";")[0]), Float.parseFloat(sound.split(";")[1]));
        }
    }

    public void addProgressToMission(Missions type, Integer value) {
        profile.getCache().getProgress(type).setValue(value);
        profile.getCache().getMissionsProgress(true).remove(type.getId());
        profile.getCache().getMissionsProgress(true).put(type.getId(), value);
    }

    public Profile getProfile() {
        return profile;
    }
}
