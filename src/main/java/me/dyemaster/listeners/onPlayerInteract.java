package me.dyemaster.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.utils.ItemChecker.isSign;

public class onPlayerInteract implements Listener {

    private static NamespacedKey namespacedKey = new NamespacedKey(getPlugin(), "custom_id");

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if the player right-clicked a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            // Check if the block is a sign
            if (block != null && isSign(block.getType())) {
                // Check if the player is holding dye
                ItemStack itemInHand = event.getItem();
                if (itemInHand != null && itemInHand.hasItemMeta() && itemInHand.getItemMeta().getPersistentDataContainer().has(namespacedKey)) {
                    event.setCancelled(true);
                }
            }
        }
    }


}
