package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierScreenHandler;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerScreenHandler;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterScreenHandler;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorScreenHandler;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserScreenHandler;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorScreenHandler;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ScreenRegistry {

    public static MenuType<CellAnalyzerScreenHandler> CELL_ANALYZER_SCREEN_HANDLER = IMenuTypeExtension.create((id, inventory, data) -> new CellAnalyzerScreenHandler(id, inventory, data.readBlockPos()));
    public static MenuType<DnaExtractorScreenHandler> DNA_EXTRACTOR_SCREEN_HANDLER = IMenuTypeExtension.create((id, inventory, data) -> new DnaExtractorScreenHandler(id, inventory, data.readBlockPos()));
    public static MenuType<DnaDecrypterScreenHandler> DNA_DECRYPTER_SCREEN_HANDLER = IMenuTypeExtension.create((id, inventory, data) -> new DnaDecrypterScreenHandler(id, inventory, data.readBlockPos()));
    public static MenuType<PlasmidInfuserScreenHandler> PLASMID_INFUSER_SCREEN_HANDLER = IMenuTypeExtension.create((id, inventory, data) -> new PlasmidInfuserScreenHandler(id, inventory, data.readBlockPos()));
    public static MenuType<BloodPurifierScreenHandler> BLOOD_PURIFIER_SCREEN_HANDLER = IMenuTypeExtension.create((id, inventory, data) -> new BloodPurifierScreenHandler(id, inventory, data.readBlockPos()));
    public static MenuType<PlasmidInjectorScreenHandler> PLASMID_INJECTOR_SCREEN_HANDLER = IMenuTypeExtension.create((id, inventory, data) -> new PlasmidInjectorScreenHandler(id, inventory, data.readBlockPos()));

    public static void registerScreens() {
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "cell_analyzer_menu"), CELL_ANALYZER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_extractor_menu"), DNA_EXTRACTOR_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_decrypter_menu"), DNA_DECRYPTER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_infuser_menu"), PLASMID_INFUSER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "blood_purifier_menu"), BLOOD_PURIFIER_SCREEN_HANDLER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_injector_menu"), PLASMID_INJECTOR_SCREEN_HANDLER);
    }
}
