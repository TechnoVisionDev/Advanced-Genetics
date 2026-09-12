package com.technovision.advancedgenetics.api.blockentity;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;

import org.jetbrains.annotations.Nullable;

public interface ProcessingBlockEntity {

    void tick();

    void updateRecipe();

    boolean canProcessRecipe();

    void processRecipe();

    <T extends Recipe<SingleRecipeInput>> void setRecipe(@Nullable T pRecipe);

    Recipe<SingleRecipeInput> getRecipe();

    int getProgress();

    void setProgress(int pProgress);

    void incrementProgress();

    void dropContents();
}
