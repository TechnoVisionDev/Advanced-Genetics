package com.technovision.advancedgenetics.api.screen.slot;

import com.technovision.advancedgenetics.common.item.SyringeItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SyringeSlot extends Slot {

    public SyringeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof SyringeItem;
    }
}
