package me.dyemaster.commands;

import me.dyemaster.guis.menus.HubMenu;
import me.dyemaster.managers.DyeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.listeners.onChatMessage.waitingForInput;

public class Dyes implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only admins can use this command!");
            return false;
        }
        if (!p.isOp()) {
            p.sendMessage(ChatColor.RED + "You can't use this command!");
            return false;
        }

        if (p.hasMetadata("DyeManager")) {//useless if all works fine
            p.removeMetadata("DyeManager", getPlugin());
        }

        waitingForInput.remove(p.getUniqueId());

        DyeManager manager = new DyeManager();
        manager.saveInPlayer(p);

        HubMenu menu = new HubMenu(manager);
        menu.displayTo(p);

        return true;
    }

}
