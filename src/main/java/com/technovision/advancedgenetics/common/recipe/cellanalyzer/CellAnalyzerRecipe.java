package com.technovision.advancedgenetics.common.recipe.cellanalyzer;

import com.technovision.advancedgenetics.api.recipe.AbstractGeneticsRecipe;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CellAnalyzerRecipe extends AbstractGeneticsRecipe {

    private final Identifier id;
    private final ItemStack input;
    private final ItemStack output;

    public CellAnalyzerRecipe(Identifier id, ItemStack input, ItemStack output) {
        super(id);
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return !world.isClient();
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(1, Ingredient.ofStacks(input));
    }

    @Override
    public String toString(){
        return String.format("input=%s, outputs=%s", input, output);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CellAnalyzerRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CellAnalyzerRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "cell_analyzer";
    }
}
