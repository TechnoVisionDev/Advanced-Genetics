package com.technovision.advancedgenetics.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public abstract class AbstractGeneticsRecipe implements Recipe<SingleRecipeInput> {
    @Override
    public boolean matches(SingleRecipeInput input, Level level) { return false; }
    @Override
    public ItemStack assemble(SingleRecipeInput input) { return ItemStack.EMPTY; }
    @Override
    public boolean isSpecial() { return true; }
    @Override
    public boolean showNotification() { return false; }
    @Override
    public String group() { return ""; }
    @Override
    public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }
    @Override
    public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
}
