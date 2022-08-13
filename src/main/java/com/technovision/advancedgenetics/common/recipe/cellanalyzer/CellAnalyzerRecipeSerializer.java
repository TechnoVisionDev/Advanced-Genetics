package com.technovision.advancedgenetics.common.recipe.cellanalyzer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;

public class CellAnalyzerRecipeSerializer implements RecipeSerializer<CellAnalyzerRecipe> {

    public static final CellAnalyzerRecipeSerializer INSTANCE = new CellAnalyzerRecipeSerializer();
    public static final String ID = CellAnalyzerRecipe.Type.ID;

    @Override
    public CellAnalyzerRecipe read(Identifier id, JsonObject json) {
        ItemStack input = ShapedRecipe.outputFromJson(json.getAsJsonObject("input"));
        ItemStack output = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));
        return new CellAnalyzerRecipe(id, input, output);
    }

    @Override
    public CellAnalyzerRecipe read(Identifier id, PacketByteBuf buf) {
        ItemStack input = buf.readItemStack();
        ItemStack output = buf.readItemStack();
        return new CellAnalyzerRecipe(id, output, input);
    }

    @Override
    public void write(PacketByteBuf buf, CellAnalyzerRecipe recipe) {
        buf.writeItemStack(recipe.getInput());
        buf.writeItemStack(recipe.getOutput());
    }
}
