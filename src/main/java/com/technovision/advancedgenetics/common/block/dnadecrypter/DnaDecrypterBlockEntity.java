package com.technovision.advancedgenetics.common.block.dnadecrypter;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.api.genetics.GeneHandler;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreenHandler;
import com.technovision.advancedgenetics.registry.BlockEntityRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class DnaDecrypterBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    protected final PropertyDelegate propertyDelegate;

    public DnaDecrypterBlockEntity(BlockPos pos, BlockState state) {
        super(DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY), BlockEntityRegistry.DNA_DECRYPTER_BLOCK_ENTITY, pos, state, Config.Common.dnaDecrypterEnergyCapacity.get(), Config.Common.dnaDecrypterTicksPerOperation.get());
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> getProgress();
                    case 1 -> getMaxProgress();
                    case 2 -> (int) getEnergyStorage().getAmount();
                    case 3 -> (int) getEnergyStorage().getCapacity();
                    case 4 -> getOverclock();
                    default -> 0;
                };
            }
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> setProgress(value);
                    case 2 -> insertEnergy(value);
                    case 4 -> setOverclock(value);
                }
            }
            public int size() {
                return 5;
            }
        };
    }

    @Override
    public void updateRecipe() { }

    @Override
    public boolean canProcessRecipe() {
        return !getStackInSlot(INPUT_SLOT_INDEX).isEmpty()
                && getStackInSlot(OUTPUT_SLOT_INDEX).isEmpty()
                && getEnergyStorage().getAmount() >= Config.Common.dnaDecrypterEnergyPerTick.get();
    }

    @Override
    public void processRecipe() {
        if (getProgress() < getMaxProgress()) {
            incrementProgress();
        } else {
            setProgress(0);
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.dnaDecrypterSuccessRate.get()) {
                ItemStack output = new ItemStack(ItemRegistry.DNA_HELIX);
                GeneHandler.setGene(input, output);
                setOrIncrement(OUTPUT_SLOT_INDEX, output);
            }
            decrementSlot(INPUT_SLOT_INDEX, 1);
        }
        extractEnergy(Config.Common.dnaDecrypterEnergyPerTick.get());
        markDirty();
    }

    @Override
    public <T extends Recipe<SimpleInventory>> void setRecipe(@Nullable T recipe) { }

    @Override
    public Recipe<SimpleInventory> getRecipe() {
        return null;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DnaDecrypterScreenHandler(syncId, inv, this, this, propertyDelegate);
    }
}
