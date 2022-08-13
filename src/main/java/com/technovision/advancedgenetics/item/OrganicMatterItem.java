package com.technovision.advancedgenetics.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class OrganicMatterItem extends Item {

    private final EntityType entityType;

    public OrganicMatterItem(EntityType entityType) {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB));
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
