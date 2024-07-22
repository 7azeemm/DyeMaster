package me.dyemaster.utils;

import me.dyemaster.DyeMaster;
import me.dyemaster.models.Dye;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static me.dyemaster.DyeMaster.getDyes;
import static me.dyemaster.DyeMaster.getPlugin;

public class DyeUtils {

    private static final NamespacedKey namespacedKey = new NamespacedKey(DyeMaster.getPlugin(), "DyeID");
    private static final NamespacedKey namespacedKey2 = new NamespacedKey(getPlugin(), "custom_id");

    public static void saveID(ItemStack item, UUID id) {

        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(namespacedKey, PersistentDataType.STRING, id.toString());

        item.setItemMeta(meta);

    }

    public static UUID getID(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        String id = data.get(namespacedKey2, PersistentDataType.STRING);

        if (id != null) return UUID.fromString(id);

        return null;
    }

    public static boolean isDye(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(namespacedKey2);
    }

    public static Dye findDyeById(UUID id) {
        return getDyes().stream()
                .filter(dye -> dye.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
