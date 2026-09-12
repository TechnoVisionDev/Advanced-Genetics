package com.technovision.advancedgenetics.common.block.plasmidinjector;

import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.util.ItemData;
import com.technovision.advancedgenetics.api.blockentity.AbstractInventoryBlockEntity;
import com.technovision.advancedgenetics.common.item.AntiPlasmidItem;
import com.technovision.advancedgenetics.common.item.PlasmidItem;
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

public class PlasmidInjectorBlockEntity extends AbstractInventoryBlockEntity {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT_INDEX = 0;
    public static final int OUTPUT_SLOT_INDEX = 1;

    public PlasmidInjectorBlockEntity(BlockPos pos, BlockState state) {
        super(NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY),
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
        return !input.isEmpty() && !output.isEmpty() && ItemData.has(input) && ItemData.has(output)
                && getEnergyStorage().getAmountAsLong() >= getEnergyRequirement()
                && ItemData.read(input).getIntOr("count", 0) >= PlasmidItem.maxGenes()
                && ItemData.read(output).getBooleanOr("filled", false)
                && ItemData.read(output).getBooleanOr("purified", false);
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
                if (input.getItem() instanceof AntiPlasmidItem) {
                    SyringeItem.addAntiGene(input, output);
                } else {
                    SyringeItem.addGene(input, output);
                }
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
        return new PlasmidInjectorScreenHandler(syncId, inv, this, this, getPropertyDelegate());
    }

    private int getEnergyRequirement() {
        return Config.Common.plasmidInjectorEnergyPerTick.get() + (Config.Common.overclockEnergy.get() * getOverclock());
    }
}
