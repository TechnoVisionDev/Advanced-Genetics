package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.entity.EntityType;

public enum Entities {
    COW("cow", EntityType.COW, "d0bb94"),
    PIG("pig", EntityType.PIG, "F9A195"),
    CHICKEN("chicken", EntityType.CHICKEN, "c9c5c5"),
    SHEEP("sheep", EntityType.SHEEP, "FFFFFF"),
    SQUID("squid", EntityType.SQUID, "3581cc"),
    HORSE("horse", EntityType.HORSE, "8c885e"),
    MOOSHROOM("mooshroom", EntityType.MOOSHROOM, "f5626e"),
    SPIDER("spider", EntityType.SPIDER, "4f5154"),
    CAVE_SPIDER("cave_spider", EntityType.CAVE_SPIDER, "546870"),
    CREEPER("creeper", EntityType.CREEPER, "8FE38F"),
    ZOMBIE("zombie", EntityType.ZOMBIE, "648c6a"),
    DROWNED("drowned", EntityType.DROWNED, "4d6487"),
    HUSK("husk", EntityType.HUSK, "9a9e8b"),
    SKELETON("skeleton", EntityType.SKELETON, "FFFFFF"),
    WITHER_SKELETON("wither_skeleton", EntityType.WITHER_SKELETON, "494a46"),
    STRAY("stray", EntityType.STRAY, "7e97a6");

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
