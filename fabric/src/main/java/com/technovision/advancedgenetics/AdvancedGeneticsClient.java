package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierScreen;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreen;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterScreen;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreen;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserScreen;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorScreen;
import com.technovision.advancedgenetics.events.ClientKeyInputEvents;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class AdvancedGeneticsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register screens
        MenuScreens.register(ScreenRegistry.CELL_ANALYZER_SCREEN_HANDLER, CellAnalyzerScreen::new);
        MenuScreens.register(ScreenRegistry.DNA_EXTRACTOR_SCREEN_HANDLER, DnaExtractorScreen::new);
        MenuScreens.register(ScreenRegistry.DNA_DECRYPTER_SCREEN_HANDLER, DnaDecrypterScreen::new);
        MenuScreens.register(ScreenRegistry.PLASMID_INFUSER_SCREEN_HANDLER, PlasmidInfuserScreen::new);
        MenuScreens.register(ScreenRegistry.BLOOD_PURIFIER_SCREEN_HANDLER, BloodPurifierScreen::new);
        MenuScreens.register(ScreenRegistry.PLASMID_INJECTOR_SCREEN_HANDLER, PlasmidInjectorScreen::new);

        // Register key binding events
        ClientKeyInputEvents.registerClientSide();
    }
}
