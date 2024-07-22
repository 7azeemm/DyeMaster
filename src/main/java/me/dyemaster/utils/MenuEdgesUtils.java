package me.dyemaster.utils;

import me.dyemaster.guis.Fill;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuEdgesUtils {

    private static final Material FILLED_ITEM = Material.BLACK_STAINED_GLASS_PANE;

    public static List<Integer> fill(Inventory inventory, Fill fill) {
        if (fill != null) {
            if (fill.getStyle() == Fill.Style.ALL) {
                return fillAll(inventory, fill.getExceptions());
            } else if (fill.getStyle() == Fill.Style.EDGES) {
                return fillEdges(inventory, fill.getExceptions());
            }
        }
        return null;
    }


    public static List<Integer> fillAll(Inventory inv, int[] exceptions) {
        Set<Integer> exceptionsSet = Arrays.stream(exceptions).boxed().collect(Collectors.toSet());
        ItemStack filledItem = new ItemStack(FILLED_ITEM);
        ItemMeta meta = filledItem.getItemMeta();
        meta.setDisplayName(" ");
        filledItem.setItemMeta(meta);
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < inv.getSize(); i++) {
            if (exceptionsSet.contains(i)) continue;
            inv.setItem(i, filledItem);
            slots.add(i);
        }

        return slots;
    }

    public static List<Integer> fillEdges(Inventory inv, int[] exceptions) {
        ItemStack filledItem = new ItemStack(FILLED_ITEM);
        ItemMeta meta = filledItem.getItemMeta();
        meta.setDisplayName(" ");
        filledItem.setItemMeta(meta);

        List<Integer> indices = getEdgeIndices(inv.getSize() / 9, exceptions);
        List<Integer> slots = new ArrayList<>();

        for (int i : indices) {
            inv.setItem(i, filledItem);
            slots.add(i);
        }

        return slots;
    }

    public static List<Integer> getEdgeIndices(int height, int[] exceptions) {
        List<Integer> edgeIndices = new ArrayList<>();
        int width = 9;

        // Top edge
        for (int i = 0; i < width; i++) {
            edgeIndices.add(i);
        }

        // Bottom edge
        for (int i = (height - 1) * width; i < height * width; i++) {
            edgeIndices.add(i);
        }

        // Left edge (excluding corners already added)
        for (int i = width; i < (height - 1) * width; i += width) {
            edgeIndices.add(i);
        }

        // Right edge (excluding corners already added)
        for (int i = 2 * width - 1; i < (height - 1) * width; i += width) {
            edgeIndices.add(i);
        }

        // Exceptions
        for (int ex : exceptions) {
            edgeIndices.remove(Integer.valueOf(ex));
        }

        return edgeIndices;
    }
}
