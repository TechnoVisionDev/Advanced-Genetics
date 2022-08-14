package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipe;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipeSerializer;
import com.technovision.advancedgenetics.common.recipe.dnaextractor.DnaExtractorRecipe;
import com.technovision.advancedgenetics.common.recipe.dnaextractor.DnaExtractorRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipeRegistry {

    public static void registerRecipes() {
        // Cell Analyzer
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AdvancedGenetics.MOD_ID, CellAnalyzerRecipeSerializer.ID), CellAnalyzerRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(AdvancedGenetics.MOD_ID, CellAnalyzerRecipe.Type.ID), CellAnalyzerRecipe.Type.INSTANCE);

        // DNA Extractor
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(AdvancedGenetics.MOD_ID, DnaExtractorRecipeSerializer.ID), DnaExtractorRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(AdvancedGenetics.MOD_ID, DnaExtractorRecipe.Type.ID), DnaExtractorRecipe.Type.INSTANCE);
    }
}
