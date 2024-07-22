package me.dyemaster.storage;

import me.dyemaster.models.Dye;
import me.dyemaster.models.Settings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.storage.Deserialization.deserializeDyeSettings;
import static me.dyemaster.storage.Serialization.serializeDyeSettings;

public class Actions {

    public static List<Dye> loadDyes() {
        File file = new File(getPlugin().getDataFolder(), "dyes.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Set<String> keys = config.getKeys(false);

        List<Dye> dyeList = new ArrayList<>();
        for (String key : keys) {
            ConfigurationSection dyeSection = config.getConfigurationSection(key);
            UUID id = UUID.fromString(key); // Assuming the key is the ID
            Boolean toggle = dyeSection.getBoolean("toggle");
            UUID creator = UUID.fromString(dyeSection.getString("creator"));
            Settings settings = deserializeDyeSettings(dyeSection.getConfigurationSection("settings"));
            String createdDate = dyeSection.getString("createdDate");
            Dye dye = new Dye(id, toggle, creator, settings, createdDate);
            dyeList.add(dye);
        }

        return dyeList;
    }

    public static void saveDye(Dye dye) {
        File file = new File(getPlugin().getDataFolder(), "dyes.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String dyeKey = dye.getId().toString();
        ConfigurationSection dyeSection = config.createSection(dyeKey);
        dyeSection.set("id", dye.getId().toString());
        dyeSection.set("toggle", dye.getToggle());
        dyeSection.set("creator", dye.getCreator().toString());
        serializeDyeSettings(dyeSection.createSection("settings"), dye.getSettings());
        dyeSection.set("createdDate", dye.getCreatedDate());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editDye(UUID dyeId, Boolean newToggle, Settings newSettings) {
        File file = new File(getPlugin().getDataFolder(), "dyes.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String dyeKey = dyeId.toString();
        ConfigurationSection dyeSection = config.getConfigurationSection(dyeKey);

        if (dyeSection == null) {
            System.out.println("Dye with ID " + dyeId + " does not exist.");
            return;
        }

        // Update the properties
        if (newToggle != null) {
            dyeSection.set("toggle", newToggle);
        }
        if (newSettings != null) {
            serializeDyeSettings(dyeSection.createSection("settings"), newSettings);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeDye(Dye dye) {
        File file = new File(getPlugin().getDataFolder(), "dyes.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String dyeKey = dye.getId().toString();

        config.set(dyeKey, null);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
