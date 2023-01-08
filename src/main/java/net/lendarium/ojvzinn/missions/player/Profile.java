package net.lendarium.ojvzinn.missions.player;

import net.lendarium.ojvzinn.missions.database.DataBase;
import net.lendarium.ojvzinn.missions.database.databases.SQLite;
import net.lendarium.ojvzinn.missions.player.cache.ProfileCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Profile {

    private static final ConcurrentHashMap<String, Profile> PROFILES = new ConcurrentHashMap<>();
    private String name;
    private ProfileCache cache;

    public static ConcurrentHashMap<String, Profile> loadProfiles() {
        return PROFILES;
    }

    public static Profile getProfile(String name) {
        return PROFILES.get(name);
    }

    public static void destroyProfile(String name) {
        getProfile(name).destroy();
        PROFILES.remove(name);
    }

    public static void createProfile(String name) {
        Profile profile = new Profile(name);
        PROFILES.put(name, profile);
    }

    public Profile(String name) {
        this.name = name;
        this.cache = new ProfileCache(this);
        loadSync();
    }

    public void saveSync() {
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).updateStatusPlayer(getName(), "ProfileMissions", "PROGRESS", this.getCache().getMissionsProgress());
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).updateStatusPlayer(getName(), "ProfileMissions", "COMPLETED", this.getCache().getMissionsCompleted());
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).updateStatusPlayer(getName(), "ProfileMissions", "SELECTED", this.getCache().getMissionsSelected());
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).updateStatusPlayer(getName(), "ProfileMissions", "AUTOMISSION", String.valueOf(this.getCache().getAutoMission()));
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).connection = null;
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
        this.name = null;
        this.cache = null;
    }

    public void loadSync() {
        if (Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).conteinsPlayer(getName(), "ProfileMissions")) {
            this.getCache().setMissionsProgress(Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).getStatusForPlayerString(getName(), "PROGRESS", "ProfileMissions"));
            this.getCache().setMissionsSelected(Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).getStatusForPlayerString(getName(), "SELECTED", "ProfileMissions"));
            this.getCache().setMissionsCompleted(Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).getStatusForPlayerString(getName(), "COMPLETED", "ProfileMissions"));
            this.getCache().setAutoMission(Boolean.valueOf(Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).getStatusForPlayerString(getName(), "AUTOMISSION", "ProfileMissions")));
            Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).connection = null;
            Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
        } else {
            Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).addStatusDefaultPlayer(getName(), "ProfileMissions");
        }
    }

    public String getName() {
        return name;
    }

    public ProfileCache getCache() {
        return cache;
    }

    public void destroy() {
        saveSync();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.getName());
    }
}
