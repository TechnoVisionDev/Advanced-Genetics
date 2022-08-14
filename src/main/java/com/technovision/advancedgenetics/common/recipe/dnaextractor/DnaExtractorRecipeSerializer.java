package com.technovision.advancedgenetics.common.recipe.dnaextractor;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;

public class DnaExtractorRecipeSerializer implements RecipeSerializer<DnaExtractorRecipe> {

    public static final DnaExtractorRecipeSerializer INSTANCE = new DnaExtractorRecipeSerializer();
    public static final String ID = DnaExtractorRecipe.Type.ID;

    @Override
    public DnaExtractorRecipe read(Identifier id, JsonObject json) {
        ItemStack input = ShapedRecipe.outputFromJson(json.getAsJsonObject("input"));
        ItemStack output = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));
        return new DnaExtractorRecipe(id, input, output);
    }

    @Override
    public DnaExtractorRecipe read(Identifier id, PacketByteBuf buf) {
        ItemStack input = buf.readItemStack();
        ItemStack output = buf.readItemStack();
        return new DnaExtractorRecipe(id, output, input);
    }

    @Override
    public void write(PacketByteBuf buf, DnaExtractorRecipe recipe) {
        buf.writeItemStack(recipe.getInput());
        buf.writeItemStack(recipe.getOutput());
    }
}
