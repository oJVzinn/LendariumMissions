package net.lendarium.ojvzinn.missions.menu;

import net.lendarium.ojvzinn.missions.library.ItemUtils;
import net.lendarium.ojvzinn.missions.library.menu.types.MenuPlayer;
import net.lendarium.ojvzinn.missions.missions.MissionsManager;
import net.lendarium.ojvzinn.missions.missions.object.Missions;
import net.lendarium.ojvzinn.missions.missions.type.MissionsType;
import net.lendarium.ojvzinn.missions.player.Profile;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class MenuPrincipalMissions extends MenuPlayer {

    public MenuPrincipalMissions(Player player) {
        super(4, "Menu principal de missões");

        List<Missions> MISSIONS = Missions.getMissionsCache();
        Profile profile = Profile.getProfile(player.getName());
        MissionsManager manager = profile.getCache().getManager();

        long max = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.BUILD_BLOCKS)).count();
        long owner = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.BUILD_BLOCKS) && manager.isCompleted(missions)).count();
        this.setItem(ItemUtils.getItemStackFromString("45:0 : 1 : nome>&eMissão de colocar bloco : desc>&7Nessa missão você tem que colocar;;&7os blocos que pedem no chão ou parades.;;&7→ &fCompletas: &a" + owner + "/" + max), 10);

        max = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.KILL_MOBS)).count();
        owner = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.KILL_MOBS) && manager.isCompleted(missions)).count();
        this.setItem(ItemUtils.getItemStackFromString("383:54 : 1 : nome>&eMissão de matar Mobs : desc>&7Nessa missão você terá que matar;;&7monstros ou animais para completar.;;&7→ &fCompletas: &a" + owner + "/" + max), 12);

        max = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.BLOCK_BREAK)).count();
        owner = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.BLOCK_BREAK) && manager.isCompleted(missions)).count();
        this.setItem(ItemUtils.getItemStackFromString("257:0 : 1 : nome>&eMissão de quebrar blocos : desc>&7Nessa missão você tem que ir no;;&7mundo da mina e quebrar os blocos.;;&7→ &fCompletas: &a" + owner + "/" + max + " : esconder>tudo"), 13);

        max = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.MOVE_BLOCKS)).count();
        owner = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.MOVE_BLOCKS) && manager.isCompleted(missions)).count();
        this.setItem(ItemUtils.getItemStackFromString("309:0 : 1 : nome>&eMissão para andar blocos : desc>&7Você precisa andar a quantia de;;&7bloco que está pedindo na missão.;;&7→ &fCompletas: &a" + owner + "/" + max), 14);

        max = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.SELL_ORES)).count();
        owner = MISSIONS.stream().filter(missions -> missions.getMissionsType().equals(MissionsType.SELL_ORES) && manager.isCompleted(missions)).count();
        this.setItem(ItemUtils.getItemStackFromString("266:0 : 1 : nome>&eMissão de vender minérios : desc>&7Na loja oficial do servidor, você;;&7terá que vender minérios que;;&7consegue na mina do factions.;;&7→ &fCompletas: &a" + owner + "/" + max), 16);

        if (MISSIONS.stream().filter(manager::isCompleted).count() == MISSIONS.size()) {
            this.setItem(ItemUtils.getItemStackFromString("395:0 : 1 : nome>&eTodas as missões foram completas &c❤ : desc>&7Muito obrigado por terminar todas as missões;;&7do factions, você dedicou muito tempo nisso, e por;;&7isso recebeu esse &b[MVP]&7, aproveite bastante ele!"), 31);
        } else {
            if (profile.getCache().getSelectedMissions() == 0) {
                this.setItem(ItemUtils.getItemStackFromString("340:0 : 1 : nome>&aNenhuma missão em andamento : desc>&7Vá em alguns dos menus e selecione uma;;&7das missões para poder começar."), 31);
            } else {
                String a = String.valueOf(profile.getCache().getMaxSelected() - profile.getCache().getSelectedMissions());
                this.setItem(ItemUtils.getItemStackFromString("386:0 : 1 : nome>&aMissões em andamento... : desc>" + profile.getCache().getCurrentMissionsSelected() + ";;&cVocê ainda pode iniciar mais " + a + " missões."), 31);
            }
        }

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
                if (event.getCurrentItem().getType() != Material.AIR) {
                    Player player = (Player) event.getWhoClicked();

                    if (event.getSlot() == 10) {
                        new MenuMissions(player, MissionsType.BUILD_BLOCKS);
                    } else if (event.getSlot() == 12) {
                        new MenuMissions(player, MissionsType.KILL_MOBS);
                    } else if (event.getSlot() == 13) {
                        new MenuMissions(player, MissionsType.BLOCK_BREAK);
                    } else if (event.getSlot() == 14) {
                        new MenuMissions(player, MissionsType.MOVE_BLOCKS);
                    } else if (event.getSlot() == 16) {
                        new MenuMissions(player, MissionsType.SELL_ORES);
                    }
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                }
            }
            event.setCancelled(true);
        }
    }

    @Override
    public void cancel() {
        HandlerList.unregisterAll(this);
    }
}
