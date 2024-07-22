package me.dyemaster.models;

import org.bukkit.Material;

public class Recipe {

    Material[][] slots;

    public Recipe(Material[][] slots) {
        this.slots = slots;
    }

    public Recipe() {
    }

    public Material[][] getSlots() {
        return slots;
    }
}