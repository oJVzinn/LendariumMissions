package net.lendarium.ojvzinn.missions.menu;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.library.ItemUtils;
import net.lendarium.ojvzinn.missions.library.menu.types.MenuPlayer;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
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

public class MenuConfirmation extends MenuPlayer {

    private final Missions missions;

    public MenuConfirmation(Player player, Missions missions) {
        super("Confirmação");
        this.missions = missions;

        this.setItem(ItemUtils.getItemStackFromString("35:5 : 1 : nome>&aConfirmar Operação : desc> &c* &7Esta operação resetará todo;;&7o seu progresso."), 12);
        this.setItem(ItemUtils.getItemStackFromString("35:14 : 1 : nome>&cCancelar Operação : desc> &c* &7Esta operação não resetará todo;;&7o seu progresso."), 14);

        register();
        open(player);
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
                    if (event.getCurrentItem().getType() != Material.AIR) {
                        if (event.getSlot() == 12) {
                            profile.getCache().removeSelectedMission(missions);
                            profile.getCache().resetProgress(missions);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                        } else if (event.getSlot() == 14) {
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                            new MenuMissions(player, missions.getMissionsType());
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void cancel() {
        HandlerList.unregisterAll(this);
    }
}
