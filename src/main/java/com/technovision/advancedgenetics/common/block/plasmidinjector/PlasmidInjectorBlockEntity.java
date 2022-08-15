package com.technovision.advancedgenetics.common.block.plasmidinjector;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserScreenHandler;
import com.technovision.advancedgenetics.common.item.PlasmidItem;
import com.technovision.advancedgenetics.registry.BlockEntityRegistry;
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

public class PlasmidInjectorBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    public PlasmidInjectorBlockEntity(BlockPos pos, BlockState state) {
        super(DefaultedList.ofSize(SLOT_COUNT, ItemStack.EMPTY),
                BlockEntityRegistry.PLASMID_INJECTOR_BLOCK_ENTITY,
                pos, state,
                Config.Common.plasmidInjectorEnergyCapacity.get(),
                Config.Common.plasmidInjectorTicksPerOperation.get(),
                Config.Common.plasmidInjectorMaxOverclock.get()
        );
    }

    @Override
    public void updateRecipe() { }

    @Override
    public boolean canProcessRecipe() {
        ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
        ItemStack output = getStackInSlot(OUTPUT_SLOT_INDEX);
        return !input.isEmpty() && !output.isEmpty() && input.hasNbt()
                && getEnergyStorage().getAmount() >= getEnergyRequirement()
                && input.getNbt().getBoolean("decoded")
                && PlasmidItem.canCombine(input, output);
    }

    @Override
    public void processRecipe() {
        if (getProgress() < getMaxProgress()) {
            incrementProgress();
        } else {
            setProgress(0);
            ItemStack input = getStackInSlot(INPUT_SLOT_INDEX);
            if (ThreadLocalRandom.current().nextDouble() <= Config.Common.plasmidInjectorSuccessRate.get()) {
                ItemStack output = getStackInSlot(OUTPUT_SLOT_INDEX);
                PlasmidItem.combine(input, output);
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
        return new PlasmidInjectorScreenHandler(syncId, inv, this, this, getPropertyDelegate());
    }

    private int getEnergyRequirement() {
        return Config.Common.plasmidInjectorEnergyPerTick.get() + (Config.Common.overclockEnergy.get() * getOverclock());
    }
}
