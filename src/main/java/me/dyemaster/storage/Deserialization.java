package me.dyemaster.storage;

import me.dyemaster.models.Icon;
import me.dyemaster.models.Recipe;
import me.dyemaster.models.Settings;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class Deserialization {
    protected static Settings deserializeDyeSettings(ConfigurationSection section) {
        String name = section.getString("name");
        String color = section.getString("color");
        Icon icon = deserializeIcon(section.getConfigurationSection("icon"));
        List<String> lore = section.getStringList("lore");
        String rawLore = section.getString("rawLore");
        Recipe recipe = deserializeRecipe(section.getConfigurationSection("recipe"));
        if (lore.isEmpty()) lore = null;
        return new Settings(name, color, icon, (ArrayList<String>) lore, rawLore, recipe);
    }

    private static Icon deserializeIcon(ConfigurationSection section) {
        Material headType = Material.getMaterial(section.getString("headType"));
        String headTexture = section.getString("headTexture");
        return new Icon(headType, headTexture);
    }

    private static Recipe deserializeRecipe(ConfigurationSection section) {
        Material[][] grid = new Material[3][3];
        boolean exists = false;
        for (int i = 0; i < grid.length; i++) {
            String[] line = section.getStringList("line" + (i + 1)).toArray(new String[0]);
            for (int y = 0; y < line.length; y++) {
                if (line[y].isEmpty()) {
                    grid[i][y] = null;
                    continue;
                }
                exists = true;
                grid[i][y] = Material.getMaterial(line[y]);
            }
        }
        if (exists) return new Recipe(grid);
        return null;
    }
}
