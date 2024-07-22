package me.dyemaster.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class ItemChecker {

    private static final Map<Material, String> LEATHER_ARMOR = Map.of(
            Material.LEATHER_BOOTS, "Leather Boots",
            Material.LEATHER_LEGGINGS, "Leather Pants",
            Material.LEATHER_CHESTPLATE, "Leather Tunic",
            Material.LEATHER_HELMET, "Leather Cap",
            Material.LEATHER_HORSE_ARMOR, "Leather Horse Armor"
    );

    public static String getArmorName(Material type) {
        return LEATHER_ARMOR.get(type);
    }

    public static boolean isLeatherArmor(ItemStack item) {
        return LEATHER_ARMOR.keySet().contains(item.getType());
    }

    private static final Set<Material> SIGN_MATERIALS = EnumSet.of(
            Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.OAK_HANGING_SIGN, Material.OAK_WALL_HANGING_SIGN,
            Material.SPRUCE_SIGN, Material.SPRUCE_WALL_SIGN, Material.SPRUCE_HANGING_SIGN, Material.SPRUCE_WALL_HANGING_SIGN,
            Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN, Material.BIRCH_HANGING_SIGN, Material.BIRCH_WALL_HANGING_SIGN,
            Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN, Material.JUNGLE_HANGING_SIGN, Material.JUNGLE_WALL_HANGING_SIGN,
            Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, Material.ACACIA_HANGING_SIGN, Material.ACACIA_WALL_HANGING_SIGN,
            Material.DARK_OAK_SIGN, Material.DARK_OAK_WALL_SIGN, Material.DARK_OAK_HANGING_SIGN, Material.DARK_OAK_WALL_HANGING_SIGN,
            Material.CRIMSON_SIGN, Material.CRIMSON_WALL_SIGN, Material.CRIMSON_HANGING_SIGN, Material.CRIMSON_WALL_HANGING_SIGN,
            Material.WARPED_SIGN, Material.WARPED_WALL_SIGN, Material.WARPED_HANGING_SIGN, Material.WARPED_WALL_HANGING_SIGN,
            Material.MANGROVE_SIGN, Material.MANGROVE_WALL_SIGN, Material.MANGROVE_HANGING_SIGN, Material.MANGROVE_WALL_HANGING_SIGN,
            Material.CHERRY_SIGN, Material.CHERRY_WALL_SIGN, Material.CHERRY_HANGING_SIGN, Material.CHERRY_WALL_HANGING_SIGN,
            Material.BAMBOO_SIGN, Material.BAMBOO_WALL_SIGN, Material.BAMBOO_HANGING_SIGN, Material.BAMBOO_WALL_HANGING_SIGN
    );

    public static boolean isSign(Material material) {
        return SIGN_MATERIALS.contains(material);
    }
}
