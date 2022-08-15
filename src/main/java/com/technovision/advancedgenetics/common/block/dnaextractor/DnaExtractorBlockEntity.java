package com.technovision.advancedgenetics.common.block.dnaextractor;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.item.DnaItem;
import com.technovision.advancedgenetics.registry.BlockEntityRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class DnaExtractorBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    public DnaExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY),
                BlockEntityRegistry.DNA_EXTRACTOR_BLOCK_ENTITY,
                pos, state,
                Config.Common.dnaExtractorEnergyCapacity.get(),
                Config.Common.dnaExtractorTicksPerOperation.get(),
                Config.Common.dnaExtractorMaxOverclock.get()
        );
    }

    @Override
    public void updateRecipe() { }

    @Override
    public boolean canProcessRecipe() {
        return !getStackInSlot(INPUT_SLOT_INDEX).isEmpty()
                && getStackInSlot(OUTPUT_SLOT_INDEX).isEmpty()
                && getEnergyStorage().getAmount() >= getEnergyRequirement();
    }

    @Override
    public void processRecipe() {
        if (getProgress() < getMaxProgress()) {
            incrementProgress();
        } else {
            setProgress(0);
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.dnaExtractorSuccessRate.get()) {
                ItemStack output = new ItemStack(ItemRegistry.DNA_HELIX);
                DnaItem.setGene(input, output);
                setOrIncrement(OUTPUT_SLOT_INDEX, output);
            }
            decrementSlot(INPUT_SLOT_INDEX, 1);
        }
        extractEnergy(getEnergyRequirement());
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
        return new DnaExtractorScreenHandler(syncId, inv, this, this, getPropertyDelegate());
    }

    private int getEnergyRequirement() {
        return Config.Common.dnaExtractorEnergyPerTick.get() + (Config.Common.overclockEnergy.get() * getOverclock());
    }
}
