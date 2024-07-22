package me.dyemaster.models;

import java.util.ArrayList;

public class Settings implements Cloneable {

    private String name;
    private String color;
    private Icon icon;
    private ArrayList<String> lore;
    private String rawLore;
    private Recipe recipe;

    public Settings(String name, String color, Icon icon, ArrayList<String> lore, String rawLore, Recipe recipe) {
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.lore = lore;
        this.rawLore = rawLore;
        this.recipe = recipe;
    }

    public Settings() {
    }

    @Override
    public Settings clone() throws CloneNotSupportedException {
        Settings settings = (Settings) super.clone();
        return settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public String getRawLore() {
        return rawLore;
    }

    public void setRawLore(String rawLore) {
        this.rawLore = rawLore;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
