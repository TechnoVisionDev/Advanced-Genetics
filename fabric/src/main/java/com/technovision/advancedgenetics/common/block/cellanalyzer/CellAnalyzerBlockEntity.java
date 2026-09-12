package com.technovision.advancedgenetics.common.block.cellanalyzer;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipe;
import com.technovision.advancedgenetics.registry.BlockEntityRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class CellAnalyzerBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    private CellAnalyzerRecipe recipe;

    public CellAnalyzerBlockEntity(BlockPos pos, BlockState state) {
        super(NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY),
                BlockEntityRegistry.CELL_ANALYZER_BLOCK_ENTITY,
                pos, state,
                Config.Common.cellAnalyzerEnergyCapacity.get(),
                Config.Common.cellAnalyzerTicksPerOperation.get(),
                Config.Common.cellAnalyzerMaxOverclock.get()
        );
    }

    @Override
    public void updateRecipe() {
        if (level == null || level.isClientSide()) return;
        if (!getStackInSlot(INPUT_SLOT_INDEX).isEmpty()) {
            level.getServer().getRecipeManager().getRecipeFor(CellAnalyzerRecipe.Type.INSTANCE,
                    new SingleRecipeInput(getStackInSlot(INPUT_SLOT_INDEX)), level)
                    .ifPresent(holder -> this.recipe = holder.value());
        }
    }

    @Override
    public boolean canProcessRecipe() {
        if (recipe != null) {
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            ItemStack output = getStackInSlot(OUTPUT_SLOT_INDEX);
            return getEnergyStorage().getAmount() >= getEnergyRequirement()
                    && (ItemStack.isSameItemSameComponents(input, recipe.getInput()) && input.getCount() >= recipe.getInput().getCount())
                    && (recipe.getOutput().getCount() + output.getCount()) <= recipe.getOutput().getMaxStackSize()
                    && (ItemStack.isSameItemSameComponents(output, recipe.getOutput()) || output.isEmpty());
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
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.cellAnalyzerSuccessRate.get()) {
                setOrIncrement(OUTPUT_SLOT_INDEX, recipe.getOutput().copy());
            }
        }
        extractEnergy(getEnergyRequirement());
        setChanged();
    }

    @Override
    public <T extends Recipe<SingleRecipeInput>> void setRecipe(@Nullable T recipe) {
        this.recipe = (CellAnalyzerRecipe) recipe;
    }

    @Override
    public Recipe<SingleRecipeInput> getRecipe() {
        return recipe;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new CellAnalyzerScreenHandler(syncId, inv, this, this, getPropertyDelegate());
    }

    private int getEnergyRequirement() {
        return Config.Common.cellAnalyzerEnergyPerTick.get() + (Config.Common.overclockEnergy.get() * getOverclock());
    }
}
