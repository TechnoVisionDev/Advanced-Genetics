package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierScreen;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreen;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterScreen;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreen;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserScreen;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorScreen;
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
        HandledScreens.register(ScreenRegistry.DNA_EXTRACTOR_SCREEN_HANDLER, DnaExtractorScreen::new);
        HandledScreens.register(ScreenRegistry.DNA_DECRYPTER_SCREEN_HANDLER, DnaDecrypterScreen::new);
        HandledScreens.register(ScreenRegistry.PLASMID_INFUSER_SCREEN_HANDLER, PlasmidInfuserScreen::new);
        HandledScreens.register(ScreenRegistry.BLOOD_PURIFIER_SCREEN_HANDLER, BloodPurifierScreen::new);
        HandledScreens.register(ScreenRegistry.PLASMID_INJECTOR_SCREEN_HANDLER, PlasmidInjectorScreen::new);

        // Dynamically dolor cells
        ItemRegistry.CELLS.values().forEach(item -> ColorProviderRegistry.ITEM.register(item::getColor, item));
    }
}
