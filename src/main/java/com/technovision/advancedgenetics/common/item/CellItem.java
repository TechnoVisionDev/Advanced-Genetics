package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.AdvancedGenetics;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CellItem extends Item {

    private final EntityType entityType;
    private final int color;

    public CellItem(EntityType entityType, String color) {
        super(new FabricItemSettings().group(AdvancedGenetics.TAB));
        this.entityType = entityType;
        this.color = (int) Long.parseLong(color, 16);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getColor(ItemStack pItemStack, int tintIndex) {
        return tintIndex > 0 ? -1 : color;
    }
}
