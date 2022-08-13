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
        ItemRegistry.CELLS.forEach(item -> ColorProviderRegistry.ITEM.register(item::getColor, item));
    }
}
