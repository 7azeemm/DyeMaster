package me.dyemaster;

import me.dyemaster.commands.Dyes;
import me.dyemaster.listeners.onDisconnect;
import me.dyemaster.listeners.onInventoryInteract;
import me.dyemaster.listeners.onPlayerInteract;
import me.dyemaster.listeners.onPrepareItemCraft;
import me.dyemaster.managers.ConfigManager;
import me.dyemaster.models.Dye;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static me.dyemaster.storage.Actions.loadDyes;
import static me.dyemaster.utils.RecipeUtils.loadRecipes;

public final class DyeMaster extends JavaPlugin {

    public static List<Dye> Dyes = new ArrayList<>();
    public static DyeMaster plugin;
    public static ConfigManager configManager;

    @Override
    public void onEnable() {

        plugin = this;

        saveDefaultConfig();
        configManager = new ConfigManager();

        getCommand("dyes").setExecutor(new Dyes());
        getServer().getPluginManager().registerEvents(new onInventoryInteract(), this);
        getServer().getPluginManager().registerEvents(new onDisconnect(), this);
        getServer().getPluginManager().registerEvents(new onPrepareItemCraft(), this);
        getServer().getPluginManager().registerEvents(new onPlayerInteract(), this);

        Dyes = loadDyes();
        loadRecipes(Dyes);
    }

    public static List<Dye> getDyes() {
        return Dyes;
    }

    public static DyeMaster getPlugin() {
        return plugin;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}

