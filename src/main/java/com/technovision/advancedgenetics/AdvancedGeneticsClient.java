package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreen;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class AdvancedGeneticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register screens
        HandledScreens.register(ScreenRegistry.CELL_ANALYZER_SCREEN_HANDLER, CellAnalyzerScreen::new);

        // Dynamically dolor cells
        ColorProviderRegistry.ITEM.register(ItemRegistry.COW_CELL::getColor, ItemRegistry.COW_CELL);
        ColorProviderRegistry.ITEM.register(ItemRegistry.PIG_CELL::getColor, ItemRegistry.PIG_CELL);
        ColorProviderRegistry.ITEM.register(ItemRegistry.CHICKEN_CELL::getColor, ItemRegistry.CHICKEN_CELL);
        ColorProviderRegistry.ITEM.register(ItemRegistry.SHEEP_CELL::getColor, ItemRegistry.SHEEP_CELL);
        ColorProviderRegistry.ITEM.register(ItemRegistry.SQUID_CELL::getColor, ItemRegistry.SQUID_CELL);
    }
}
