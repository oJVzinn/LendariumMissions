package net.lendarium.ojvzinn.missions.library.menu.types;

import net.lendarium.ojvzinn.missions.library.menu.MenuAbstract;

public abstract class MenuPlayer extends MenuAbstract {

    public MenuPlayer(Integer rows, String title) {
        super(rows, title);
    }

    public MenuPlayer(String title) {
        super(title);
    }
}
