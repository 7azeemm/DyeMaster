package me.dyemaster.managers;

import static me.dyemaster.DyeMaster.getPlugin;

public class ConfigManager {

    public boolean dyeNameColor = getPlugin().getConfig().getBoolean("DyeNameColor");
    public boolean armorNameColor = getPlugin().getConfig().getBoolean("ArmorNameColor");

    public void setArmorNameColor(boolean armorNameColor) {
        this.armorNameColor = armorNameColor;
    }

    public void setDyeNameColor(boolean dyeNameColor) {
        this.dyeNameColor = dyeNameColor;
    }

    public boolean isDyeNameColor() {
        return dyeNameColor;
    }

    public boolean isArmorNameColor() {
        return armorNameColor;
    }
}
