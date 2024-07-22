package me.dyemaster.listeners;


import me.dyemaster.guis.Button;
import me.dyemaster.guis.Menu;
import me.dyemaster.managers.DyeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.guis.menus.CreationMenu.RecipeMenu.updateStatus;

public class onInventoryInteract implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (player.hasMetadata("DyeManager")) {
            final int slot = event.getRawSlot();
            final ItemStack item = event.getCurrentItem();
            DyeManager manager = (DyeManager) player.getMetadata("DyeManager").getFirst().value();
            Menu menu = manager.getMenu();

            if (player.getOpenInventory().getTitle().equals(menu.getTitle())) {

                for (final Button button : menu.getButtons()) {
                    if (button.getSlot() == slot) {
                        event.setCancelled(true);
                        button.onClick(player);
                        return;
                    }
                }

                if (menu.getEdgeSlots() != null) {
                    for (int edgeSlot : menu.getEdgeSlots()) {
                        if (slot == edgeSlot) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }

                if (item != null && item.getType() != Material.AIR) {
                    if (!menu.isCancel()) {
                        if (menu.getBlackListedMaterials().contains(item.getType()) || menu.getBlackListedSlots().contains(slot)) {
                            event.setCancelled(true);
                            return;
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateStatus(player.getOpenInventory().getTopInventory());
                            }
                        }.runTask(getPlugin());
                        return;
                    }
                }

                if (!menu.isCancel()) {
                    if (player.getItemOnCursor().getType() != Material.AIR) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                updateStatus(player.getOpenInventory().getTopInventory());
                            }
                        }.runTask(getPlugin());
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (player.hasMetadata("DyeManager")) {
            DyeManager manager = (DyeManager) player.getMetadata("DyeManager").getFirst().value();
            Menu menu = manager.getMenu();
            if (!menu.isCancel()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        updateStatus(player.getOpenInventory().getTopInventory());
                    }
                }.runTask(getPlugin());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player dude = (Player) event.getPlayer();

        if (dude.hasMetadata("DyeManager")) {
            DyeManager manager = (DyeManager) dude.getMetadata("DyeManager").getFirst().value();
            if (manager.isSwitchingMenus()) {
                manager.setSwitchingMenus(false);
                return;
            }

            if (manager.getAction() != null && manager.getAction() != DyeManager.Action.EDIT && manager.getAction() != DyeManager.Action.CREATE)
                return;

            dude.removeMetadata("DyeManager", getPlugin());
        }
    }
}
