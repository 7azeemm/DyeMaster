package me.dyemaster.listeners;

import me.dyemaster.DyeMaster;
import me.dyemaster.models.Dye;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.utils.ColorsUtils.hexToRGB;
import static me.dyemaster.utils.DyeUtils.*;
import static me.dyemaster.utils.ItemChecker.getArmorName;
import static me.dyemaster.utils.ItemChecker.isLeatherArmor;

public class onPrepareItemCraft implements Listener {

    private static final NamespacedKey namespacedKey = new NamespacedKey(getPlugin(), "custom_id");
    private static final NamespacedKey namespacedKey2 = new NamespacedKey(getPlugin(), "custom_dye_id");
    private static final NamespacedKey namespacedKey3 = new NamespacedKey(getPlugin(), "dye_color");

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (item != null && item.hasItemMeta() && isDye(item)) {
                CraftingInventory inventory = event.getInventory();
                Dye dye = findDyeById(getID(item));

                if (dye != null) {
                    ItemStack result = event.getInventory().getResult();
                    if (result != null && result.getItemMeta() != null && result.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
                        return;
                    }

                    int num = 0;
                    for (ItemStack i : inventory.getMatrix()) {
                        if (i != null) num++;
                    }
                    if (num > 2 || num == 1) {
                        event.getInventory().setResult(new ItemStack(Material.AIR));
                        return;
                    }

                    for (ItemStack ingredient : inventory.getMatrix()) {
                        if (ingredient == null) continue;
                        boolean isLeather = isLeatherArmor(ingredient);
                        if (isLeather) {
                            if (!event.getView().getPlayer().hasPermission("dyemaster.dyearmor")) {
                                event.getInventory().setResult(new ItemStack(Material.AIR));
                                return;
                            }
                            ItemStack clone = ingredient.clone();
                            colorArmor(clone, dye.getId().toString(), dye.getSettings().getColor());
                            event.getInventory().setResult(clone);
                            return;
                        }
                    }

                    event.getInventory().setResult(new ItemStack(Material.AIR));
                    return;
                }
                return;
            }
        }


        ItemStack result = event.getInventory().getResult();
        if (result != null && result.hasItemMeta() && isDye(result)) {
            if (!event.getView().getPlayer().hasPermission("dyemaster.craftdye")) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    private static void colorArmor(ItemStack armor, String id, String color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        if (DyeMaster.getConfigManager().isArmorNameColor()) {
            if (meta.hasDisplayName()) {
                meta.setDisplayName(ChatColor.of(color) + ChatColor.stripColor(meta.getDisplayName()));
            } else {
                String name = getArmorName(armor.getType());
                meta.setDisplayName(ChatColor.of(color) + name);
            }
        }
        meta.setColor(Color.fromRGB(hexToRGB(color)));
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(namespacedKey2, PersistentDataType.STRING, id);
        data.set(namespacedKey3, PersistentDataType.STRING, color);
        armor.setItemMeta(meta);
    }
}
