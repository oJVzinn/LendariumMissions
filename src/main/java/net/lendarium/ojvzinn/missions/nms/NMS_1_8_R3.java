package net.lendarium.ojvzinn.missions.nms;

import net.lendarium.ojvzinn.missions.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMS_1_8_R3 {

    public static void sendActionBar(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    public static void sendTitle(Player player, String helder, String footer) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.sendTitle(helder, footer);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            craftPlayer.sendTitle("", "");
        }, 20 * 3);
    }
}
