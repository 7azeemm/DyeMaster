package me.dyemaster.models;

import me.dyemaster.DyeMaster;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static me.dyemaster.DyeMaster.getPlugin;
import static me.dyemaster.utils.CustomSkull.createHead;
import static me.dyemaster.utils.CustomSkull.getProfile;

public class Dye {
    private UUID id;
    private Boolean toggle;
    private UUID creator;
    private Settings settings;
    private String createdDate;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ItemStack create() {
        String name = settings.getName();
        String color = settings.getColor();
        Icon icon = settings.getIcon();
        ArrayList<String> lore = settings.getLore();

        ItemStack item;

        if (icon.getHeadType() == Material.PLAYER_HEAD) {
            PlayerProfile profile = getProfile(icon.getHeadTexture());
            item = createHead(profile);
        } else item = new ItemStack(icon.getHeadType());

        ItemMeta meta = item.getItemMeta();

        if (DyeMaster.getConfigManager().isDyeNameColor()) {
            meta.setDisplayName(ChatColor.of(color) + name);
        } else {
            meta.setDisplayName(ChatColor.WHITE + name);
        }
        meta.setLore(lore);

        PersistentDataContainer container = meta.getPersistentDataContainer();

        container.set(new NamespacedKey(getPlugin(), "custom_id"), PersistentDataType.STRING, id.toString());
        container.set(new NamespacedKey(getPlugin(), "UniqueID"), PersistentDataType.STRING, UUID.randomUUID().toString());

        item.setItemMeta(meta);
        return item;
    }

    public void giveTo(Player player) {
        ItemStack item = create();
        player.getInventory().addItem(item);
    }

    public Dye(UUID creator, Settings settings) {
        this.id = UUID.randomUUID();
        this.toggle = true;
        this.creator = creator;
        this.settings = settings;
        this.createdDate = dateFormat.format(new Date());
    }

    public Dye(UUID id, Boolean toggle, UUID creator, Settings settings, String createdDate) {
        this.id = id;
        this.toggle = toggle;
        this.creator = creator;
        this.settings = settings;
        this.createdDate = createdDate;
    }

    public Boolean getToggle() {
        return toggle;
    }

    public void setToggle(Boolean toggle) {
        this.toggle = toggle;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCreator() {
        return creator;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
