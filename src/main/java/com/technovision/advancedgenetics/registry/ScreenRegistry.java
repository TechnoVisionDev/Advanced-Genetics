package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ScreenRegistry {

    public static ExtendedScreenHandlerType<CellAnalyzerScreenHandler> CELL_ANALYZER_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CellAnalyzerScreenHandler::new);

    public static void registerScreens() {
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer_menu"), CELL_ANALYZER_SCREEN_HANDLER);
    }
}
