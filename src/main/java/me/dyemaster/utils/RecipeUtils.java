package me.dyemaster.utils;

import me.dyemaster.models.Dye;
import me.dyemaster.models.Recipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

import static me.dyemaster.DyeMaster.getPlugin;

public class RecipeUtils {

    public static Recipe saveRecipe(Inventory menu) {

        int[][] slots = {
                {12, 13, 14},
                {21, 22, 23},
                {30, 31, 32}
        };

        Material[][] grid = new Material[3][3];

        for (int i = 0; i < slots.length; i++) {
            for (int y = 0; y < slots[i].length; y++) {
                if (menu.getItem(slots[i][y]) == null) {
                    grid[i][y] = null;
                } else grid[i][y] = menu.getItem(slots[i][y]).getType();
            }
        }

        return new Recipe(grid);
    }

    public static void loadRecipes(List<Dye> dyes) {
        for (Dye dye : dyes) {
            if (dye.getToggle()) {
                Recipe recipe = dye.getSettings().getRecipe();
                if (recipe != null) {
                    registerRecipe(dye);
                }
            }
        }
    }

    public static void unloadRecipes(List<Dye> dyes) {
        for (Dye dye : dyes) {
            Recipe recipe = dye.getSettings().getRecipe();
            if (recipe != null) {
                Bukkit.removeRecipe(new NamespacedKey(getPlugin(), dye.getId().toString()));
            }
        }
    }

    public static void reloadRecipes(List<Dye> dyes) {
        unloadRecipes(dyes);
        loadRecipes(dyes);
    }

    public static void registerRecipe(Dye dye) {
        NamespacedKey key = new NamespacedKey(getPlugin(), String.valueOf(dye.getId()));
        ShapedRecipe recipe = new ShapedRecipe(key, dye.create());
        Material[][] slots = dye.getSettings().getRecipe().getSlots();

        recipe.shape("abc", "def", "ghi");
        for (int i = 0; i < 3; i++) {
            for (int y = 0; y < 3; y++) {
                int j = (3 * i) + y;
                if (slots[i][y] == null) continue;
                recipe.setIngredient((char) (j + 'a'), slots[i][y]);
            }
        }

        Bukkit.addRecipe(recipe);
    }

    public static void unregisterRecipe(NamespacedKey namespacedKey) {
        Bukkit.removeRecipe(namespacedKey);
    }
}
