package com.technovision.advancedgenetics.common.recipe.cellanalyzer;

import com.technovision.advancedgenetics.api.recipe.AbstractGeneticsRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CellAnalyzerRecipe extends AbstractGeneticsRecipe {
    private final ItemStackTemplate input;
    private final ItemStackTemplate output;

    public CellAnalyzerRecipe(ItemStackTemplate input, ItemStackTemplate output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(SingleRecipeInput inventory, Level level) {
        return !level.isClientSide() && ItemStack.isSameItemSameComponents(inventory.item(), getInput());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput inventory) { return output.create(); }
    public ItemStack getInput() { return input.create(); }
    public ItemStackTemplate getInputTemplate() { return input; }
    public ItemStack getOutput() { return output.create(); }
    public ItemStackTemplate getOutputTemplate() { return output; }

    @Override
    public String toString() { return String.format("input=%s, outputs=%s", input, output); }
    @Override
    public RecipeSerializer<CellAnalyzerRecipe> getSerializer() { return CellAnalyzerRecipeSerializer.INSTANCE; }
    @Override
    public RecipeType<CellAnalyzerRecipe> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<CellAnalyzerRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "cell_analyzer";
    }
}
