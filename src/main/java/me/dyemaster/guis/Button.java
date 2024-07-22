package me.dyemaster.guis;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class Button {

    private int slot;
    private UUID id;

    public Button(int slot) {
        this.slot = slot;
    }

    public Button() {
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public final int getSlot() {
        return slot;
    }

    public abstract ItemStack getItem();

    public abstract void onClick(Player player);
}