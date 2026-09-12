package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierScreen;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreen;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterScreen;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreen;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserScreen;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorScreen;
import com.technovision.advancedgenetics.events.ClientKeyInputEvents;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.client.gui.screens.MenuScreens;

@Mod(value = AdvancedGenetics.MOD_ID, dist = Dist.CLIENT)
public class AdvancedGeneticsClient {
    public AdvancedGeneticsClient(IEventBus bus) {
        bus.addListener(AdvancedGeneticsClient::screens);
        bus.addListener(ClientKeyInputEvents::registerClientSide);
        NeoForge.EVENT_BUS.addListener(ClientKeyInputEvents::tick);
    }

    private static void screens(RegisterMenuScreensEvent event) {
        // Register screens
        event.register(ScreenRegistry.CELL_ANALYZER_SCREEN_HANDLER, CellAnalyzerScreen::new);
        event.register(ScreenRegistry.DNA_EXTRACTOR_SCREEN_HANDLER, DnaExtractorScreen::new);
        event.register(ScreenRegistry.DNA_DECRYPTER_SCREEN_HANDLER, DnaDecrypterScreen::new);
        event.register(ScreenRegistry.PLASMID_INFUSER_SCREEN_HANDLER, PlasmidInfuserScreen::new);
        event.register(ScreenRegistry.BLOOD_PURIFIER_SCREEN_HANDLER, BloodPurifierScreen::new);
        event.register(ScreenRegistry.PLASMID_INJECTOR_SCREEN_HANDLER, PlasmidInjectorScreen::new);

        // Register key binding events

    }
}
