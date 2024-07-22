package me.dyemaster.managers;

import me.dyemaster.guis.menus.CreationMenu;
import me.dyemaster.models.Settings;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.managers.DyeManager.getManager;
import static me.dyemaster.utils.ColorsUtils.isValidHexColor;
import static me.dyemaster.utils.ColorsUtils.loreProcess;

public class AnvilManager {

    public static void openAnvil(Player p, String title, String defaultText, DyeManager.Action action, DyeManager.Action menuAction) {
        defaultText = defaultText == null ? " " : defaultText;

        DyeManager manager = getManager(p);
        Settings settings = manager.getSettings();
        manager.setAction(action);

        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> {
                    manager.setAction(menuAction);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new CreationMenu(manager).displayTo(p);
                        }
                    }.runTask(getPlugin());
                })
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText().trim();
                    if (action == DyeManager.Action.NAME) {
                        if (text.isEmpty() || text.isBlank()) {
                            p.sendMessage(ChatColor.RED + "Please enter a valid name!");
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                            settings.setName(text);
                        }
                    } else if (action == DyeManager.Action.COLOR) {
                        if (text.isEmpty() || text.isBlank()) {
                            p.sendMessage(ChatColor.RED + "Please enter a hex color!");
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        } else {
                            if (isValidHexColor(text)) {
                                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                                settings.setColor(text);
                                if (settings.getLore() != null)
                                    settings.setLore(loreProcess(settings.getRawLore(), text));
                            } else {
                                p.sendMessage(ChatColor.RED + "Please enter a valid hex color code #RRGGBB");
                                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            }
                        }
                    }

                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                .text(defaultText)
                .title(title)
                .plugin(getPlugin())
                .open(p);
    }
}
