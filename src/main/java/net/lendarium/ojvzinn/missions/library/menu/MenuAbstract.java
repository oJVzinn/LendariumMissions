package net.lendarium.ojvzinn.missions.library.menu;

import net.lendarium.ojvzinn.missions.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class MenuAbstract implements Listener {

    private final Inventory inventory;

    public MenuAbstract(Integer rows, String title) {
        this.inventory = Bukkit.getServer().createInventory(null, rows * 9, title);
    }

    public MenuAbstract(String title) {
        this.inventory = Bukkit.getServer().createInventory(null, 3 * 9, title);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void setItem(ItemStack item, Integer slot) {
        this.inventory.setItem(slot, item);
    }

    public abstract void cancel();
}
