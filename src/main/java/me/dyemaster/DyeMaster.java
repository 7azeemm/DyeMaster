package me.dyemaster;

import me.dyemaster.commands.Dyes;
import me.dyemaster.listeners.onDisconnect;
import me.dyemaster.listeners.onInventoryInteract;
import me.dyemaster.listeners.onPlayerInteract;
import me.dyemaster.listeners.onPrepareItemCraft;
import me.dyemaster.models.Dye;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static me.dyemaster.storage.Actions.loadDyes;
import static me.dyemaster.utils.RecipeUtils.loadRecipes;

public final class DyeMaster extends JavaPlugin {

    public static List<Dye> Dyes = new ArrayList<>();


    public static DyeMaster plugin;

//    @Override
//    public void onLoad() {
//        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
//        PacketEvents.getAPI().getSettings()
//                .reEncodeByDefault(false)
//                .checkForUpdates(true)
//                .bStats(false);
//        PacketEvents.getAPI().load();
//    }

    public boolean dyeNameColor = getConfig().getBoolean("DyeNameColor");
    public boolean armorNameColor = getConfig().getBoolean("ArmorNameColor");

    public void setArmorNameColor(boolean armorNameColor) {
        this.armorNameColor = armorNameColor;
    }

    public void setDyeNameColor(boolean dyeNameColor) {
        this.dyeNameColor = dyeNameColor;
    }

    public boolean isDyeNameColor() {
        return dyeNameColor;
    }

    public boolean isArmorNameColor() {
        return armorNameColor;
    }

    @Override
    public void onEnable() {

        //warning maybe for unsave changes in goback

        //TODO: remove all "this"?
        //TODO: config for : colored dye name item as color chosen + dye color applied to armor in display name or lore (color + text or icon)
        plugin = this;

        saveDefaultConfig();

//        PacketEvents.getAPI().init();
//        PacketEvents.getAPI().getEventManager().registerListener(new onSignUpdate(), PacketListenerPriority.NORMAL);

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

//    @Override
//    public void onDisable() {
//        PacketEvents.getAPI().terminate();
//    }
}

