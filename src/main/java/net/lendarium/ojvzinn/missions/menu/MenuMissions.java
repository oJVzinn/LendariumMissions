package net.lendarium.ojvzinn.missions.menu;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.library.ItemUtils;
import net.lendarium.ojvzinn.missions.library.menu.types.MenuPagedAbstract;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.missions.type.MissionsType;
import net.lendarium.ojvzinn.missions.nms.NMS_1_8_R3;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class MenuMissions extends MenuPagedAbstract {

    private final Map<Integer, Missions> MISSION_SLOT = new HashMap<>();
    private final MissionsType type;

    public MenuMissions(Player player, MissionsType type) {
        super(6, "Missões de " + type.getName().toLowerCase(Locale.ROOT));
        onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
        Profile profile = Profile.getProfile(player.getName());
        List<ItemStack> ITENS = new ArrayList<>();
        List<Missions> MISSIONS_FILTER = Missions.getMissionsCache().stream().filter(missions -> missions.getMissionsType().equals(type)).collect(Collectors.toList());
        this.type = type;

        for (int i = 0; i < MISSIONS_FILTER.size(); i++) {
            Integer slot = this.getOnlySlotsList().get(i);
            Missions missions = MISSIONS_FILTER.get(i);
            if (profile.getCache().isSelectedMission(missions)) {
                ITENS.add(ItemUtils.putGlowInItem(missions.getIconStringFinal(Profile.getProfile(player.getName()), missions)));
            } else {
                ITENS.add(missions.getIconStringFinal(Profile.getProfile(player.getName()), missions));
            }
            MISSION_SLOT.put(slot, MISSIONS_FILTER.get(i));
        }

        this.setItem(ItemUtils.getItemStackFromString("ARROW:0 : 1 : nome>&cVoltar"), 45);
        String ativated = profile.getCache().getAutoMission() ? "&aAtivado" : "&cDesativado";
        this.setItem(ItemUtils.getItemStackFromString("131:0 : 1 : nome>&eMissões automáticas : desc>&7Ao ativar esta opção, você;;&7não precisa voltar a esse menu;;&7e será ativado automáticamente;;&7as próximas missões;;&f→ Ativado: " + ativated), 49);
        this.setItens(ITENS);
        this.open(player);
        this.register();
    }

    @EventHandler
    public void onPlayerQuitListners(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.getOpenInventory().getTopInventory().equals(this.getInventory())) {
            cancel();
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (event.getInventory().equals(this.getInventory())) {
            cancel();
        }
    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event) {
        if (event.getInventory().equals(getInventory())) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                Profile profile = Profile.getProfile(player.getName());
                if (event.getCurrentItem().getType() != Material.AIR) {
                    Missions missions = MISSION_SLOT.get(event.getSlot());
                    if (missions != null) {
                        if (profile.getCache().isSelectedMission(missions)) {
                            player.closeInventory();
                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> new MenuConfirmation(player, missions), 2L);
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                            return;
                        } else {
                            if (profile.getCache().canSelect(missions)) {
                                int a = profile.getCache().getMaxSelected() - profile.getCache().getSelectedMissions();
                                player.sendMessage(a == 0 ? "§cVocê não pode selecionar mais nenhuma missão!" : "§cVocê não pode selecionar esta missão.");
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                            } else {
                                profile.getCache().setSelectedMission(missions);
                                player.closeInventory();
                                NMS_1_8_R3.sendActionBar(player, "§aVocê selecionou com sucesso a missão: " + missions.getName() + "!");
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                            }
                        }
                        new MenuMissions(player, type);
                        event.setCancelled(true);
                        return;
                    }

                    if (event.getSlot() == 49) {
                        if (!player.hasPermission("missoes.automatico")) {
                            player.sendMessage("§cVocê não possui permissão para fazer isso!");
                            return;
                        }
                        profile.getCache().setAutoMission(!profile.getCache().getAutoMission());
                        new MenuMissions(player, type);
                    } else {
                        new MenuPrincipalMissions(player);
                    }
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                }
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void cancel() {
        HandlerList.unregisterAll(this);
    }
}
