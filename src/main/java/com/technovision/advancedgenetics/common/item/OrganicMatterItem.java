package com.technovision.advancedgenetics.common.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class OrganicMatterItem extends Item {
    private final EntityType<?> entityType;

    public OrganicMatterItem(Properties properties, EntityType<?> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() { return entityType; }
}
