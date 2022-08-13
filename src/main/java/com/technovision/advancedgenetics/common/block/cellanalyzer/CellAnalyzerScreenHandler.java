package com.technovision.advancedgenetics.common.block.cellanalyzer;

import com.technovision.advancedgenetics.api.screen.AbstractGeneticsScreenHandler;
import com.technovision.advancedgenetics.api.screen.slot.OrganicMatterSlot;
import com.technovision.advancedgenetics.api.screen.slot.OutputSlot;
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

public class CellAnalyzerScreenHandler extends AbstractGeneticsScreenHandler {

    protected final PropertyDelegate propertyDelegate;

    public CellAnalyzerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buffer) {
        this(syncId, playerInventory, Objects.requireNonNull(playerInventory.player.getWorld().getBlockEntity(buffer.readBlockPos())), new SimpleInventory(CellAnalyzerBlockEntity.SLOT_COUNT), new ArrayPropertyDelegate(5));
    }

    protected CellAnalyzerScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, Inventory inventory, PropertyDelegate delegate) {
        super(ScreenRegistry.CELL_ANALYZER_SCREEN_HANDLER, syncId, playerInventory, blockEntity, inventory, delegate, 1, 1);
        addSlots(OrganicMatterSlot::new, inventory, 0, 1, 63, 36);
        addSlots(OutputSlot::new, inventory, 1, 1, 110, 36);

        this.propertyDelegate = delegate;
        addProperties(delegate);
    }

    @Override
    public void addPlayerInventorySlots(Inventory inventory) {
        addSlots(Slot::new, inventory, 3, 9, 9, 27, 8, 84);
        addSlots(Slot::new, inventory, 1, 9, 0, 9, 8, 142);
    }
}
