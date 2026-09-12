package com.technovision.advancedgenetics.common.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CellItem extends Item {
    private final EntityType<?> entityType;
    private final int color;

    public CellItem(Properties properties, EntityType<?> entityType, int color) {
        super(properties);
        this.entityType = entityType;
        this.color = color;
    }

    public EntityType<?> getEntityType() { return entityType; }
    public int getColor(ItemStack stack, int tintIndex) { return tintIndex > 0 ? -1 : color; }
}
