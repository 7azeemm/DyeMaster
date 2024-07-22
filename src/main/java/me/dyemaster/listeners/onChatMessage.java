package me.dyemaster.listeners;

import me.dyemaster.guis.menus.CreationMenu;
import me.dyemaster.managers.DyeManager;
import me.dyemaster.models.Icon;
import me.dyemaster.models.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.utils.ColorsUtils.loreProcess;
import static me.dyemaster.utils.CustomSkull.getProfile;

public class onChatMessage implements Listener {

    public static Map<UUID, DyeManager.Action> waitingForInput = new HashMap<>();

    @EventHandler
    public void onChatMsg(AsyncPlayerChatEvent e) {
        if (waitingForInput.containsKey(e.getPlayer().getUniqueId())) {
            Player player = e.getPlayer();
            DyeManager manager = (DyeManager) player.getMetadata("DyeManager").getFirst().value();
            Settings settings = manager.getSettings();
            DyeManager.Action action = manager.getAction();
            manager.setAction(waitingForInput.get(player.getUniqueId()));
            waitingForInput.remove(player.getUniqueId());

            e.setCancelled(true);
            if (waitingForInput.isEmpty()) e.getHandlers().unregister(this);
            String message = e.getMessage();
            System.out.println(action);

            if (action == DyeManager.Action.SKULL) {
                PlayerProfile profile = getProfile(message);
                if (profile == null) {
                    player.sendMessage(ChatColor.RED + "Texture is invalid!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new CreationMenu(manager).displayTo(player);
                        }
                    }.runTask(getPlugin());
                    return;
                }

                Icon icon = new Icon(Material.PLAYER_HEAD, message);
                settings.setIcon(icon);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new CreationMenu(manager).displayTo(player);
                    }
                }.runTask(getPlugin());
            } else if (action == DyeManager.Action.LORE) {
                String rawLore = settings.getRawLore();
                String color = settings.getColor();

                rawLore = rawLore == null ? message : rawLore + "\\n" + message;
                settings.setRawLore(rawLore);
                settings.setLore(loreProcess(rawLore, color));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new CreationMenu(manager).new LoreMenu(manager).displayTo(player);
                    }
                }.runTask(getPlugin());
            }
        }
    }
}