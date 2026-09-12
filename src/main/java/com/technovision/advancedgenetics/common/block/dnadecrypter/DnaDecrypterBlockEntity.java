package com.technovision.advancedgenetics.common.block.dnadecrypter;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.util.ItemData;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.item.DnaItem;
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

public class DnaDecrypterBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    public DnaDecrypterBlockEntity(BlockPos pos, BlockState state) {
        super(NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY),
                BlockEntityRegistry.DNA_DECRYPTER_BLOCK_ENTITY,
                pos, state,
                Config.Common.dnaDecrypterEnergyCapacity.get(),
                Config.Common.dnaDecrypterTicksPerOperation.get(),
                Config.Common.dnaDecrypterMaxOverclock.get()
        );
    }

    @Override
    public void updateRecipe() { }

    @Override
    public boolean canProcessRecipe() {
        ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
        ItemStack output = getStackInSlot(OUTPUT_SLOT_INDEX);
        return !input.isEmpty() && ItemData.has(input)
                && getEnergyStorage().getAmountAsLong() >= getEnergyRequirement()
                && !ItemData.read(input).getBooleanOr("decoded", false)
                && 1 + output.getCount() <= output.getMaxStackSize()
                && (output.isEmpty() || ItemData.read(output).getStringOr("gene", "").equals(ItemData.read(input).getStringOr("gene", "")));
    }

    @Override
    public void processRecipe() {
        if (getProgress() < getMaxProgress()) {
            incrementProgress();
        } else {
            setProgress(0);
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.dnaDecrypterSuccessRate.get()) {
                ItemStack output = input.copy();
                output.setCount(1);
                DnaItem.decode(output);
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
        return new DnaDecrypterScreenHandler(syncId, inv, this, this, getPropertyDelegate());
    }

    private int getEnergyRequirement() {
        return Config.Common.dnaDecrypterEnergyPerTick.get() + (Config.Common.overclockEnergy.get() * getOverclock());
    }
}
