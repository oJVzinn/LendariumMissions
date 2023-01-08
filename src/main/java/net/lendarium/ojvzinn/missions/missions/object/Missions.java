package net.lendarium.ojvzinn.missions.missions.object;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.library.ItemUtils;
import net.lendarium.ojvzinn.missions.missions.type.MissionsType;
import net.lendarium.ojvzinn.missions.player.Profile;
import net.lendarium.ojvzinn.missions.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Missions {

    private static final List<Missions> MISSIONS_CACHE = new ArrayList<>();
    private final String id;
    private final MissionsType missionsType;
    private final String value;
    private final String name;
    private final String icon;
    private final String command;
    private final String reward;

    public static void setupMissions() {
        FileUtils fileUtils = new FileUtils("missions", Main.getInstance().getDescription().getName(), Missions.class);
        fileUtils.loadFileYaml();
        YamlConfiguration CONFIG = fileUtils.getYamlConfiguration();

        for (String key : CONFIG.getConfigurationSection("missions").getKeys(false)) {
            String id = CONFIG.getString("missions." + key + ".id");
            String mission = CONFIG.getString("missions." + key + ".mission");
            String value = CONFIG.getString("missions." + key + ".value");
            String name = CONFIG.getString("missions." + key + ".name");
            String icon = CONFIG.getString("missions." + key + ".icon");
            String command = CONFIG.getString("missions." + key + ".command");
            String reward = CONFIG.getString("missions." + key + ".reward");

            Missions missionsLoader = new Missions(id, mission, value, name, icon, command, reward);
            MISSIONS_CACHE.add(missionsLoader);
        }
    }

    public static void reload() {
        MISSIONS_CACHE.clear();
        setupMissions();
    }

    public static Missions getMissionFromID(String id) {
        return MISSIONS_CACHE.stream().filter(missionsLoader -> missionsLoader.getId().equals(id)).findFirst().orElse(null);
    }

    public static List<Missions> getMissionFromType(MissionsType type) {
        return MISSIONS_CACHE.stream().filter(missionsLoader -> missionsLoader.getMissionsType().equals(type)).collect(Collectors.toList());
    }

    public static List<Missions> getMissionsCache() {
        return MISSIONS_CACHE;
    }

    public Missions(String id, String missionType, String value, String name, String icon, String command, String reward) {
        this.id = id;
        this.missionsType = MissionsType.findByID(missionType);
        this.value = value;
        this.name = name;
        this.icon = icon;
        this.command = command;
        this.reward = reward;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconString() {
        return icon;
    }

    public ItemStack getIconStringFinal(Profile profile, Missions missions) {
        return ItemUtils.getItemStackFromString(icon.replace("{desc}", ";;§eInformações:;;§f→ Recompensa: " + getReward() + ";;§f→ Status: " + (profile.getCache().getManager().isCompleted(missions) ? "§aConcluída" : "§cNão concluída")));
    }

    public MissionsType getMissionsType() {
        return missionsType;
    }

    public String getValue() {
        return value;
    }

    public String getCommand() {
        return command;
    }

    public ItemStack getIcon() {
        return ItemUtils.getItemStackFromString(this.icon);
    }

    public String getTypeValue() {
        if (this.value.split(" : ").length > 1) {
            return this.value.split(" : ")[1];
        } else {
            return  null;
        }
    }

    public void setupCommand(String name) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command.replace("{player}", name));
    }

    public Integer getValueToCompleted() {
        return Integer.valueOf(this.value.split(" : ")[0]);
    }

    public String getReward() {
        return reward;
    }
}
