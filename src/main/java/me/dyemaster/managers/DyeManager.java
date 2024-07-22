package me.dyemaster.managers;

import me.dyemaster.guis.Menu;
import me.dyemaster.models.Dye;
import me.dyemaster.models.Settings;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import static me.dyemaster.DyeMaster.getPlugin;

public class DyeManager {

    Menu menu;
    Action action;
    Dye dye;
    Settings settings;
    boolean switchingMenus = false;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Dye getDye() {
        return dye;
    }

    public void setDye(Dye dye) {
        this.dye = dye;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public static DyeManager getManager(Player p) {
        if (p.hasMetadata("DyeManager")) {
            return (DyeManager) p.getMetadata("DyeManager").getFirst().value();
        }
        return null;
    }

    public void setSwitchingMenus(boolean switchingMenus) {
        this.switchingMenus = switchingMenus;
    }

    public boolean isSwitchingMenus() {
        return switchingMenus;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void saveInPlayer(Player player) {
        player.setMetadata("DyeManager", new FixedMetadataValue(getPlugin(), this));
    }

    public enum Action {
        CREATE,
        EDIT,
        NAME,
        COLOR,
        LORE,
        SKULL
    }
}
