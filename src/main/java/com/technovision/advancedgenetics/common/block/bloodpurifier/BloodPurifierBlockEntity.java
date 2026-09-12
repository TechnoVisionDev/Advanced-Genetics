package com.technovision.advancedgenetics.common.block.bloodpurifier;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.util.ItemData;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.item.SyringeItem;
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

public class BloodPurifierBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    public BloodPurifierBlockEntity(BlockPos pos, BlockState state) {
        super(NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY),
                BlockEntityRegistry.BLOOD_PURIFIER_BLOCK_ENTITY,
                pos, state,
                Config.Common.bloodPurifierEnergyCapacity.get(),
                Config.Common.bloodPurifierTicksPerOperation.get(),
                Config.Common.bloodPurifierMaxOverclock.get()
        );
    }

    @Override
    public void updateRecipe() { }

    @Override
    public boolean canProcessRecipe() {
        ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
        ItemStack output = getStackInSlot(OUTPUT_SLOT_INDEX);
        return !input.isEmpty() && output.isEmpty() && ItemData.has(input)
                && getEnergyStorage().getAmountAsLong() >= getEnergyRequirement()
                && ItemData.read(input).getBooleanOr("filled", false)
                && !ItemData.read(input).getBooleanOr("purified", false);
    }

    @Override
    public void processRecipe() {
        if (getProgress() < getMaxProgress()) {
            incrementProgress();
        } else {
            setProgress(0);
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.bloodPurifierSuccessRate.get()) {
                ItemStack output = input.copy();
                SyringeItem.purify(output);
                setOrIncrement(OUTPUT_SLOT_INDEX, output);
            }
            decrementSlot(INPUT_SLOT_INDEX, 1);
        }
        extractEnergy(getEnergyRequirement());
        setChanged();
    }

    @Override
    public <T extends Recipe<SingleRecipeInput>> void setRecipe(@Nullable T recipe) { }

    @Override
    public Recipe<SingleRecipeInput> getRecipe() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new BloodPurifierScreenHandler(syncId, inv, this, this, getPropertyDelegate());
    }

    private int getEnergyRequirement() {
        return Config.Common.bloodPurifierEnergyPerTick.get() + (Config.Common.overclockEnergy.get() * getOverclock());
    }
}
