package net.lendarium.ojvzinn.missions.player.cache;

import net.lendarium.ojvzinn.missions.missions.object.Missions;


public class MissionProgress {

    private final Missions type;
    private Integer value;

    public MissionProgress(Missions type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public Missions getType() {
        return type;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
