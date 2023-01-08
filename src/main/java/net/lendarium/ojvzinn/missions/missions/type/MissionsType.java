package net.lendarium.ojvzinn.missions.missions.type;

import java.util.Arrays;

public enum MissionsType {
    BLOCK_BREAK("Quebrar blocos", "1", "Blocos"),
    LUCKY_BREAK("Quebrar lucky blocks", "2", "Lucky Blocks"),
    MOVE_BLOCKS("Andar Blocos", "3", "Blocos"),
    BUILD_BLOCKS("Construir Blocos", "4", "Blocos"),
    SELL_ORES("Vender Minérios", "5", "Minérios"),
    KILL_MOBS("Matar Mobs", "6", "Mobs");

    private final String name;
    private final String id;
    private final String type;

    public static MissionsType findByID(String id) {
        return Arrays.stream(MissionsType.values()).filter(missionsType -> missionsType.getId().equals(id)).findFirst().orElse(null);
    }

    MissionsType(String name, String id, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
