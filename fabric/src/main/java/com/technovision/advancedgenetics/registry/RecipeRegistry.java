package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipe;
import com.technovision.advancedgenetics.common.recipe.cellanalyzer.CellAnalyzerRecipeSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class RecipeRegistry {

    public static void registerRecipes() {
        // Cell Analyzer
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, CellAnalyzerRecipeSerializer.ID), CellAnalyzerRecipeSerializer.INSTANCE);
        Registry.register(BuiltInRegistries.RECIPE_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, CellAnalyzerRecipe.Type.ID), CellAnalyzerRecipe.Type.INSTANCE);
    }
}
