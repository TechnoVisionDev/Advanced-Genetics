package com.technovision.advancedgenetics.common.block.plasmidinfuser;

import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreenHandler;
import com.technovision.advancedgenetics.api.screen.slot.DnaSlot;
import com.technovision.advancedgenetics.api.screen.slot.PlasmidSlot;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.ContainerData;

import java.util.Objects;

public class PlasmidInfuserScreenHandler extends AbstractGeneticsScreenHandler {

    protected final ContainerData propertyDelegate;

    public PlasmidInfuserScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, Objects.requireNonNull(playerInventory.player.level().getBlockEntity(pos)), new SimpleContainer(PlasmidInfuserBlockEntity.SLOT_COUNT), new SimpleContainerData(5));
    }

    protected PlasmidInfuserScreenHandler(int syncId, Inventory playerInventory, BlockEntity blockEntity, Container inventory, ContainerData delegate) {
        super(ScreenRegistry.PLASMID_INFUSER_SCREEN_HANDLER, syncId, playerInventory, blockEntity, inventory, delegate, 1, 1);
        addSlots(DnaSlot::new, inventory, 0, 1, 63, 36);
        addSlots(PlasmidSlot::new, inventory, 1, 1, 110, 36);

        this.propertyDelegate = delegate;
        addDataSlots(delegate);
    }
}
