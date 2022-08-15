package com.technovision.advancedgenetics.common.block.dnaextractor;

import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreenHandler;
import com.technovision.advancedgenetics.api.screen.slot.CellSlot;
import com.technovision.advancedgenetics.api.screen.slot.OutputSlot;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerBlockEntity;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;

import java.util.Objects;

public class DnaExtractorScreenHandler extends AbstractGeneticsScreenHandler {

    protected final PropertyDelegate propertyDelegate;

    public DnaExtractorScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buffer) {
        this(syncId, playerInventory, Objects.requireNonNull(playerInventory.player.getWorld().getBlockEntity(buffer.readBlockPos())), new SimpleInventory(DnaExtractorBlockEntity.SLOT_COUNT), new ArrayPropertyDelegate(5));
    }

    protected DnaExtractorScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, Inventory inventory, PropertyDelegate delegate) {
        super(ScreenRegistry.DNA_EXTRACTOR_SCREEN_HANDLER, syncId, playerInventory, blockEntity, inventory, delegate, 1, 1);
        addSlots(CellSlot::new, inventory, 0, 1, 63, 36);
        addSlots(OutputSlot::new, inventory, 1, 1, 110, 36);

        this.propertyDelegate = delegate;
        addProperties(delegate);
    }
}
