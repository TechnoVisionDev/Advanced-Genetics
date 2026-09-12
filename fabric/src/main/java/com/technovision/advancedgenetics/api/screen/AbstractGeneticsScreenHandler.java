package com.technovision.advancedgenetics.api.screen;

import com.mojang.datafixers.util.Function4;
import com.technovision.advancedgenetics.api.blockentity.AbstractProcessingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;

public abstract class AbstractGeneticsScreenHandler extends AbstractContainerMenu {

    private final ContainerData delegate;
    private final Container inventory;
    private final int inputSlots;
    private final int outputSlots;
    private final Level world;
    private final AbstractProcessingBlockEntity blockEntity;

    public AbstractGeneticsScreenHandler(MenuType<?> screenHandlerType, int syncId, Inventory playerInventory, BlockEntity blockEntity, Container inventory, ContainerData delegate, int inputSlots, int outputSlots) {
        super(screenHandlerType, syncId);
        this.delegate = delegate;
        this.inventory = inventory;
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
        this.world = playerInventory.player.level();
        this.blockEntity = (AbstractProcessingBlockEntity) blockEntity;
        addPlayerInventorySlots(playerInventory);
        inventory.startOpen(playerInventory.player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        int blockEntitySlots = inputSlots + outputSlots;
        Slot sourceSlot = slots.get(invSlot);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();

        if (invSlot < 36) {
            if (!moveItemStackTo(sourceStack, 36, 36 + inputSlots, false)) {
                return ItemStack.EMPTY;
            }
        } else if (invSlot < 36 + inputSlots) {
            if (!moveItemStackTo(sourceStack, 0, 36, false))  {
                return ItemStack.EMPTY;
            }
        } else if (invSlot >= 36 + inputSlots && invSlot < 36 + blockEntitySlots) {
            if (!moveItemStackTo(sourceStack, 0, 36, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    protected <T> void addSlots(Function4<Container, Integer, Integer, Integer, Slot> slotType, Container container, int xOrigin, int yOrigin) {
        addSlots(slotType, container, 1, 1, 0, 1, xOrigin, yOrigin);
    }

    protected <T> void addSlots(Function4<Container, Integer, Integer, Integer, Slot> slotType, Container container, int startIndex, int totalSlots, int xOrigin, int yOrigin) {
        addSlots(slotType, container, 1, 1, startIndex, totalSlots, xOrigin, yOrigin);
    }

    protected <T> void addSlots(Function4<Container, Integer, Integer, Integer, Slot> slotType, Container container, int rows, int columns, int startIndex, int totalSlots, int xOrigin, int yOrigin) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int slotIndex = column + row * columns + startIndex;
                int x = xOrigin + column * 18;
                int y = yOrigin + row * 18;
                if (slotIndex < startIndex + totalSlots) {
                    this.addSlot(slotType.apply(container, slotIndex, x, y));
                }
            }
        }
    }

    public void addPlayerInventorySlots(Container inventory) {
        addSlots(Slot::new, inventory, 3, 9, 9, 27, 8, 84);
        addSlots(Slot::new, inventory, 1, 9, 0, 9, 8, 142);
    }

    public ContainerData getPropertyDelegate() {
        return delegate;
    }

    public AbstractProcessingBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public Level getWorld() {
        return world;
    }

    public Container getClientInventory() {
        return inventory;
    }
}
