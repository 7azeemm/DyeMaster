package me.dyemaster.models;

import org.bukkit.Material;

public class Icon {
    private Material headType;
    private String headTexture;

    public Icon(Material headType, String headTexture) {
        this.headType = headType;
        this.headTexture = headTexture;
    }

    public Material getHeadType() {
        return headType;
    }

    public void setHeadType(Material headType) {
        this.headType = headType;
    }

    public String getHeadTexture() {
        return headTexture;
    }

    public void setHeadTexture(String headTexture) {
        this.headTexture = headTexture;
    }
}
