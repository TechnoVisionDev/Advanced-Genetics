package com.technovision.advancedgenetics.api.genetics;

import net.minecraft.entity.EntityType;

public enum Entities {
    COW("cow", EntityType.COW, "443626"),
    PIG("pig", EntityType.PIG, "f19e98"),
    CHICKEN("chicken", EntityType.CHICKEN, "e2e2e2"),
    SHEEP("sheep", EntityType.SHEEP, "dedede"),
    SQUID("squid", EntityType.SQUID, "546d80"),
    HORSE("horse", EntityType.HORSE, "b98968"),
    MOOSHROOM("mooshroom", EntityType.MOOSHROOM, "a81012"),
    SPIDER("spider", EntityType.SPIDER, "4f453c"),
    CAVE_SPIDER("cave_spider", EntityType.CAVE_SPIDER, "546870"),
    CREEPER("creeper", EntityType.CREEPER, "65d152"),
    ZOMBIE("zombie", EntityType.ZOMBIE, "3e692d"),
    DROWNED("drowned", EntityType.DROWNED, "4d9280"),
    HUSK("husk", EntityType.HUSK, "7a6849"),
    SKELETON("skeleton", EntityType.SKELETON, "bcbcbc"),
    WITHER_SKELETON("wither_skeleton", EntityType.WITHER_SKELETON, "343434"),
    STRAY("stray", EntityType.STRAY, "9caeac");

    private final String name;
    private final EntityType type;
    private final int color;

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
