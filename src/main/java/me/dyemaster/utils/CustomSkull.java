package me.dyemaster.utils;

import me.dyemaster.models.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class CustomSkull {

    public static ItemStack createIcon(Icon icon) {
        if (icon.getHeadType() == Material.PLAYER_HEAD) {
            return createHead(getProfile(icon.getHeadTexture()));
        } else {
            return new ItemStack(icon.getHeadType());
        }
    }

    public static ItemStack createHead(PlayerProfile profile) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        assert meta != null;
        meta.setOwnerProfile(profile);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack createPlayerHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(player.getDisplayName());
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);
        return item;
    }

    public static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID()); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url);
            textures.setSkin(urlObject); // Set the skin of the player profile to the URL
            profile.setTextures(textures); // Set the textures back to the profile
        } catch (MalformedURLException | IllegalArgumentException __) {
            return null;
        }
        return profile;
    }
}
