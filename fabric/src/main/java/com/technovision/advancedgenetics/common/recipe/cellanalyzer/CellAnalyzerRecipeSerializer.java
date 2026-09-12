package com.technovision.advancedgenetics.common.recipe.cellanalyzer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class CellAnalyzerRecipeSerializer {
    public static final String ID = CellAnalyzerRecipe.Type.ID;
    public static final MapCodec<CellAnalyzerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("input").forGetter(CellAnalyzerRecipe::getInputTemplate),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CellAnalyzerRecipe::getOutputTemplate)
    ).apply(instance, CellAnalyzerRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CellAnalyzerRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, CellAnalyzerRecipe::getInputTemplate,
            ItemStackTemplate.STREAM_CODEC, CellAnalyzerRecipe::getOutputTemplate,
            CellAnalyzerRecipe::new
    );
    public static final RecipeSerializer<CellAnalyzerRecipe> INSTANCE = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    private CellAnalyzerRecipeSerializer() { }
}
