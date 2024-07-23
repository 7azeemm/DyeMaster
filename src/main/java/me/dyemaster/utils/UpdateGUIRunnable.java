package me.dyemaster.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateGUIRunnable extends BukkitRunnable {

    private final Inventory gui;
    Material[] materials = new Material[]{Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE};
    int row = 0;
    int guiType;

    public UpdateGUIRunnable(Inventory gui, int guiType) {
        this.gui = gui;
        this.guiType = guiType;
    }

    @Override
    public void run() {
        updateGUIItems();
    }

    private void updateGUIItems() {
        if (gui.getViewers().isEmpty()) cancel();

        int[] firstRow;
        int[] secondRow;

        if (guiType == 0) {
            firstRow = new int[]{12, 14, 32, 30};
            secondRow = new int[]{13, 23, 31, 21};
        } else {
            firstRow = new int[]{10, 12, 30, 28};
            secondRow = new int[]{11, 21, 29, 19};
        }

        row = row == 0 ? 1 : 0;
        int y = row == 0 ? 1 : 0;

        ItemStack[] items = new ItemStack[]{new ItemStack(materials[0]), new ItemStack(materials[1])};
        for (ItemStack item : items) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
        }

        for (int i : firstRow) {
            gui.setItem(i, items[y]);
        }
        for (int i : secondRow) {
            gui.setItem(i, items[row]);
        }
    }
}
