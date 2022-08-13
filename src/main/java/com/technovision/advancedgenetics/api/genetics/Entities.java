package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.entity.EntityType;

public enum Entities {
    COW("cow", EntityType.COW, "d0bb94"),
    PIG("pig", EntityType.PIG, "F9A195"),
    CHICKEN("chicken", EntityType.CHICKEN, "c9c5c5"),
    SHEEP("sheep", EntityType.SHEEP, "FFFFFF"),
    SQUID("squid", EntityType.SQUID, "3581cc");

    private String name;
    private EntityType type;
    private int color;

    Entities(String name, EntityType type, String color) {
        this.name = name;
        this.type = type;
        this.color = (int) Long.parseLong(color, 16);
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public int getColor() {
        return color;
    }
}
