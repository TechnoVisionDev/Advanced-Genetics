package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreenHandler;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterScreenHandler;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ScreenRegistry {

    public static ExtendedScreenHandlerType<CellAnalyzerScreenHandler> CELL_ANALYZER_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CellAnalyzerScreenHandler::new);
    public static ExtendedScreenHandlerType<DnaExtractorScreenHandler> DNA_EXTRACTOR_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(DnaExtractorScreenHandler::new);
    public static ExtendedScreenHandlerType<DnaDecrypterScreenHandler> DNA_DECRYPTER_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(DnaDecrypterScreenHandler::new);

    public static void registerScreens() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer_menu"), CELL_ANALYZER_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(AdvancedGenetics.MOD_ID, "dna_extractor_menu"), DNA_EXTRACTOR_SCREEN_HANDLER);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(AdvancedGenetics.MOD_ID, "dna_decrypter_menu"), DNA_DECRYPTER_SCREEN_HANDLER);
    }
}
