package com.technovision.advancedgenetics.api.blockentity;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.recipe.Recipe;

import javax.annotation.Nullable;

public interface ProcessingBlockEntity {

    void tick();

    void updateRecipe();

    boolean canProcessRecipe();

    void processRecipe();

    <T extends Recipe<SimpleInventory>> void setRecipe(@Nullable T pRecipe);

    Recipe<SimpleInventory> getRecipe();

    int getProgress();

    void setProgress(int pProgress);

    void incrementProgress();

    void dropContents();
}
