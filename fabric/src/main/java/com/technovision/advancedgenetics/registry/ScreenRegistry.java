package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierScreenHandler;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreenHandler;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterScreenHandler;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreenHandler;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserScreenHandler;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ScreenRegistry {

    public static ExtendedMenuType<CellAnalyzerScreenHandler, BlockPos> CELL_ANALYZER_SCREEN_HANDLER = new ExtendedMenuType<>(CellAnalyzerScreenHandler::new, BlockPos.STREAM_CODEC);
    public static ExtendedMenuType<DnaExtractorScreenHandler, BlockPos> DNA_EXTRACTOR_SCREEN_HANDLER = new ExtendedMenuType<>(DnaExtractorScreenHandler::new, BlockPos.STREAM_CODEC);
    public static ExtendedMenuType<DnaDecrypterScreenHandler, BlockPos> DNA_DECRYPTER_SCREEN_HANDLER = new ExtendedMenuType<>(DnaDecrypterScreenHandler::new, BlockPos.STREAM_CODEC);
    public static ExtendedMenuType<PlasmidInfuserScreenHandler, BlockPos> PLASMID_INFUSER_SCREEN_HANDLER = new ExtendedMenuType<>(PlasmidInfuserScreenHandler::new, BlockPos.STREAM_CODEC);
    public static ExtendedMenuType<BloodPurifierScreenHandler, BlockPos> BLOOD_PURIFIER_SCREEN_HANDLER = new ExtendedMenuType<>(BloodPurifierScreenHandler::new, BlockPos.STREAM_CODEC);
    public static ExtendedMenuType<PlasmidInjectorScreenHandler, BlockPos> PLASMID_INJECTOR_SCREEN_HANDLER = new ExtendedMenuType<>(PlasmidInjectorScreenHandler::new, BlockPos.STREAM_CODEC);

    public static void registerScreens() {
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "cell_analyzer_menu"), CELL_ANALYZER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_extractor_menu"), DNA_EXTRACTOR_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_decrypter_menu"), DNA_DECRYPTER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_infuser_menu"), PLASMID_INFUSER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "blood_purifier_menu"), BLOOD_PURIFIER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_injector_menu"), PLASMID_INJECTOR_SCREEN_HANDLER);
    }
}
