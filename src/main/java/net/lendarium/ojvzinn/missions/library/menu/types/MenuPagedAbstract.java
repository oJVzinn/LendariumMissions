package net.lendarium.ojvzinn.missions.library.menu.types;

import net.lendarium.ojvzinn.missions.library.menu.MenuAbstract;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MenuPagedAbstract extends MenuAbstract {

    private final List<Integer> onlySlotsList = new ArrayList<>();
    private Integer nextPagedSlot;
    private Integer previusPagedSlot;
    private Integer lastInt;

    public MenuPagedAbstract(Integer rows, String title) {
        super(rows, title);
    }

    public MenuPagedAbstract(String title) {
        super(title);
    }

    public void onlySlots(Integer... integer) {
        this.onlySlotsList.addAll(Arrays.asList(integer));
    }

    public void setItens(List<ItemStack> itens) {
        for (int i = 0; i < itens.size(); i++) {
            int slot = onlySlotsList.get(i);
            this.setItem(itens.get(i), slot);
            if (slot == this.onlySlotsList.get(this.onlySlotsList.size() - 1)) {
                lastInt = i;
                break;
            }
        }
    }

    public Integer getLastInt() {
        return lastInt;
    }

    public Integer getNextPagedSlot() {
        return nextPagedSlot;
    }

    public Integer getPreviusPagedSlot() {
        return previusPagedSlot;
    }

    public List<Integer> getOnlySlotsList() {
        return onlySlotsList;
    }
}
