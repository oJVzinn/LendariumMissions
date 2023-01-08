package net.lendarium.ojvzinn.missions.player.cache;

import net.lendarium.ojvzinn.missions.missions.MissionsManager;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.player.Profile;
import net.lendarium.ojvzinn.missions.utils.StringUtils;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class ProfileCache {

    private final Profile profile;
    private final List<MissionProgress> PROGRESS_CACHE = new ArrayList<>();
    private final MissionsManager manager;
    private Boolean isAutoMission;
    private JSONObject missionsProgress = new JSONObject();
    private JSONObject missionsCompleted = new JSONObject();
    private JSONObject missionsSelected = new JSONObject();

    public ProfileCache(Profile profile) {
        for (Missions type : Missions.getMissionsCache()) {
            missionsCompleted.put(type.getId(), false);
            missionsProgress.put(type.getId(), 0);
            MissionProgress progress = new MissionProgress(type);
            progress.setValue(0);
            PROGRESS_CACHE.add(progress);
        }
        this.missionsSelected.put("Selected", new JSONArray());
        this.profile = profile;
        this.manager = new MissionsManager(profile);
        this.isAutoMission = false;
    }

    public Profile getProfile() {
        return profile;
    }

    public MissionsManager getManager() {
        return manager;
    }

    public void setMissionsCompleted(String jsonOBJECT) {
        try {
            this.missionsCompleted = (JSONObject) new JSONParser().parse(jsonOBJECT);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public void setMissionsProgress(String jsonOBJECT) {
        try {
            this.missionsProgress = (JSONObject) new JSONParser().parse(jsonOBJECT);
            for (Missions type : Missions.getMissionsCache()) {
                if (this.missionsProgress.containsKey(type.getId())) {
                    missionsCompleted.put(type.getId(), false);
                    missionsProgress.put(type.getId(), 0);
                    MissionProgress progress = new MissionProgress(type);
                    progress.setValue(0);
                    PROGRESS_CACHE.add(progress);
                }
            }
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public void setMissionsSelected(String jsonOBJECT) {
        try {
            this.missionsSelected = (JSONObject) new JSONParser().parse(jsonOBJECT);
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }


    public String getMissionsCompleted() {
        return this.missionsCompleted.toJSONString();
    }

    public String getMissionsSelected() {
        return this.missionsSelected.toJSONString();
    }

    public JSONObject getMissionsCompleted(boolean isJSON) {
        return this.missionsCompleted;
    }

    public String getMissionsProgress() {
        return missionsProgress.toString();
    }

    public JSONObject getMissionsProgress(boolean isJSON) {
        return missionsProgress;
    }

    public MissionProgress getProgress(Missions type) {
        return this.PROGRESS_CACHE.stream().filter(missionProgress -> missionProgress.getType().equals(type)).findFirst().orElse(null);
    }


    public List<MissionProgress> loadPROGRESS_CACHE() {
        return PROGRESS_CACHE;
    }

    public Boolean getAutoMission() {
        return isAutoMission;
    }

    public void setAutoMission(Boolean autoMission) {
        isAutoMission = autoMission;
    }

    public void setSelectedMission(Missions missions) {
        JSONArray jsonArray = (JSONArray) this.missionsSelected.get("Selected");
        jsonArray.add(missions.getId());
    }

    public boolean isSelectedMission(Missions missions) {
        JSONArray jsonArray = (JSONArray) this.missionsSelected.get("Selected");
        return jsonArray.contains(missions.getId());
    }

    public void removeSelectedMission(Missions missions) {
        JSONArray jsonArray = (JSONArray) this.missionsSelected.get("Selected");
        jsonArray.remove(missions.getId());
    }

    public Integer getSelectedMissions() {
        return ((JSONArray) this.missionsSelected.get("Selected")).size();
    }

    public Integer getMaxSelected() {
        Player player = getProfile().getPlayer();
        if (player.hasPermission("select.all")) {
            return Missions.getMissionsCache().size();
        } else if (player.hasPermission("select.10")) {
            return 10;
        } else if (player.hasPermission("select.7")){
            return 7;
        } else if (player.hasPermission("select.5")) {
            return 5;
        } else if (player.hasPermission("select.3")) {
            return 3;
        } else {
            return 1;
        }
    }

    public boolean canSelect(Missions missions) {
        JSONArray currentSelect = (JSONArray) this.missionsSelected.get("Selected");
        if (getMaxSelected() - currentSelect.size() != 0) {
            return getManager().isCompleted(missions);
        } else {
            return false;
        }
    }

    public void resetProgress(Missions missions) {
        getProgress(missions).setValue(0);
    }

    public String getProgressBar(Profile profile, Missions missions) {
        Integer max = missions.getValueToCompleted();
        Integer progress = profile.getCache().getProgress(missions).getValue();

        if (profile.getCache().getManager().isCompleted(missions)) {
            return StringUtils.repeat("§8", 10);
        }

        int percentage = progress * 100 / max;

        return StringUtils.repeat("§a▎", (percentage / 10)) + "§8" + StringUtils.repeat("▎", (10 - percentage / 10));
    }

    public String getCurrentMissionsSelected() {
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray currentSelect = (JSONArray) this.missionsSelected.get("Selected");
        for (Object id : currentSelect) {
            Missions mission = Missions.getMissionFromID((String) id);
            int faltando = mission.getValueToCompleted() - getProgress(mission).getValue();
            stringBuilder.append("§f").append(mission.getName()).append(": ").append("§eEstá faltando ").append(faltando).append(" ").append(mission.getMissionsType().getType().toLowerCase(Locale.ROOT)).append("... ").append(getProgressBar(profile, mission)).append(";;");
        }

        return stringBuilder.toString();
    }
}
