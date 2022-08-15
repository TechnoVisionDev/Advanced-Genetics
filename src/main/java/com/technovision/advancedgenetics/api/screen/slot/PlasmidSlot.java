package com.technovision.advancedgenetics.api.screen.slot;

import com.technovision.advancedgenetics.common.item.PlasmidItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class PlasmidSlot extends Slot {

    public PlasmidSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof PlasmidItem;
    }
}
