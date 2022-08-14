package com.technovision.advancedgenetics.common.block.dnaextractor;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipe;
import com.technovision.advancedgenetics.registry.BlockEntityRegistry;
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

public class DnaExtractorBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    private CellAnalyzerRecipe recipe;
    protected final PropertyDelegate propertyDelegate;

    public DnaExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY), BlockEntityRegistry.DNA_EXTRACTOR_BLOCK_ENTITY, pos, state, Config.Common.dnaExtractorEnergyCapacity.get(), Config.Common.dnaExtractorTicksPerOperation.get());
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
    public void updateRecipe() {
        if (world == null || world.isClient()) return;
        if (!getStackInSlot(INPUT_SLOT_INDEX).isEmpty()) {
             world.getRecipeManager().getAllMatches(CellAnalyzerRecipe.Type.INSTANCE, new SimpleInventory(1), world)
                    .stream()
                    .filter(recipe -> ItemStack.canCombine(getStackInSlot(0), recipe.getInput()))
                    .findFirst()
                    .ifPresent(recipe -> {
                        this.recipe = recipe;
                    });
        }
    }

    @Override
    public boolean canProcessRecipe() {
        if (recipe != null) {
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            ItemStack output = getStackInSlot(OUTPUT_SLOT_INDEX);
            return getEnergyStorage().getAmount() >= Config.Common.dnaExtractorEnergyPerTick.get()
                    && (ItemStack.canCombine(input, recipe.getInput()) && input.getCount() >= recipe.getInput().getCount())
                    && (recipe.getOutput().getCount() + output.getCount()) <= recipe.getOutput().getMaxCount()
                    && (ItemStack.canCombine(output, recipe.getOutput()) || output.isEmpty());
        }
        return false;
    }

    @Override
    public void processRecipe() {
        if (getProgress() < getMaxProgress()) {
            incrementProgress();
        } else {
            setProgress(0);
            decrementSlot(INPUT_SLOT_INDEX, recipe.getInput().getCount());
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.dnaExtractorSuccessRate.get()) {
                setOrIncrement(OUTPUT_SLOT_INDEX, recipe.getOutput().copy());
            }
        }
        extractEnergy(Config.Common.dnaExtractorEnergyPerTick.get());
        markDirty();
    }

    @Override
    public <T extends Recipe<SimpleInventory>> void setRecipe(@Nullable T recipe) {
        this.recipe = (CellAnalyzerRecipe) recipe;
    }

    @Override
    public Recipe<SimpleInventory> getRecipe() {
        return recipe;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DnaExtractorScreenHandler(syncId, inv, this, this, propertyDelegate);
    }
}
