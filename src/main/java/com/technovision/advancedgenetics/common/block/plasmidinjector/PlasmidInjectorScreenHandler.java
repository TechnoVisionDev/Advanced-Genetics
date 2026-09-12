package com.technovision.advancedgenetics.common.block.plasmidinjector;

import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreenHandler;
import com.technovision.advancedgenetics.api.screen.slot.PlasmidSlot;
import com.technovision.advancedgenetics.api.screen.slot.SyringeSlot;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.ContainerData;


public class PlasmidInjectorScreenHandler extends AbstractGeneticsScreenHandler {

    protected final ContainerData propertyDelegate;

    public PlasmidInjectorScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.level().getBlockEntity(pos), new SimpleContainer(PlasmidInjectorBlockEntity.SLOT_COUNT), new SimpleContainerData(5));
    }

    protected PlasmidInjectorScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, Container inventory, ContainerData delegate) {
        super(ScreenRegistry.PLASMID_INJECTOR_SCREEN_HANDLER, syncId, playerInventory, blockEntity, inventory, delegate, 1, 1);
        addSlots(PlasmidSlot::new, inventory, 0, 1, 63, 36);
        addSlots(SyringeSlot::new, inventory, 1, 1, 110, 36);

        this.propertyDelegate = delegate;
        addDataSlots(delegate);
    }
}
