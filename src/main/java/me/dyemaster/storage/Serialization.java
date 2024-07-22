package me.dyemaster.storage;

import me.dyemaster.models.Icon;
import me.dyemaster.models.Recipe;
import me.dyemaster.models.Settings;
import org.bukkit.configuration.ConfigurationSection;

public class Serialization {

    protected static void serializeDyeSettings(ConfigurationSection section, Settings settings) {
        section.set("name", settings.getName());
        section.set("color", settings.getColor());
        serializeIcon(section.createSection("icon"), settings.getIcon());
        section.set("lore", settings.getLore());
        section.set("rawLore", settings.getRawLore());
        serializeRecipe(section.createSection("recipe"), settings.getRecipe());
    }

    private static void serializeIcon(ConfigurationSection section, Icon icon) {
        section.set("headType", icon.getHeadType().name());
        section.set("headTexture", icon.getHeadTexture());
    }

    private static void serializeRecipe(ConfigurationSection section, Recipe recipe) {
        if (recipe == null) return;
        for (int i = 0; i < recipe.getSlots().length; i++) {
            String[] line = new String[3];
            for (int y = 0; y < recipe.getSlots()[i].length; y++) {
                if (recipe.getSlots()[i][y] == null) {
                    line[y] = "";
                } else line[y] = recipe.getSlots()[i][y].name();
            }
            section.set("line" + (i + 1), line);
        }
    }
}
