package com.technovision.advancedgenetics.api.screen.slot;

import com.technovision.advancedgenetics.common.item.SyringeItem;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;

public class SyringeSlot extends Slot {

    public SyringeSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof SyringeItem;
    }
}
