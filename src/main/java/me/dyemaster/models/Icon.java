package me.dyemaster.models;

import org.bukkit.Material;

public class Icon {
    private final Material headType;
    private final String headTexture;

    public Icon(Material headType, String headTexture) {
        this.headType = headType;
        this.headTexture = headTexture;
    }

    public Material getHeadType() {
        return headType;
    }

    public String getHeadTexture() {
        return headTexture;
    }
}
