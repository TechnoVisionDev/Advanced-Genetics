package com.technovision.advancedgenetics.api.screen.slot;

import com.technovision.advancedgenetics.common.item.OrganicMatterItem;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class OrganicMatterSlot extends Slot {

    public OrganicMatterSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof OrganicMatterItem;
    }
}
