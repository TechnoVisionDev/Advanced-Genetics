package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierBlock;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerBlock;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterBlock;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorBlock;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserBlock;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorBlock;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class BlockRegistry {

    public static final CellAnalyzerBlock CELL_ANALYZER = new CellAnalyzerBlock(settings("cell_analyzer"));
    public static final DnaExtractorBlock DNA_EXTRACTOR = new DnaExtractorBlock(settings("dna_extractor"));
    public static final DnaDecrypterBlock DNA_DECRYPTER = new DnaDecrypterBlock(settings("dna_decrypter"));
    public static final PlasmidInfuserBlock PLASMID_INFUSER = new PlasmidInfuserBlock(settings("plasmid_infuser"));
    public static final BloodPurifierBlock BLOOD_PURIFIER = new BloodPurifierBlock(settings("blood_purifier"));
    public static final PlasmidInjectorBlock PLASMID_INJECTOR = new PlasmidInjectorBlock(settings("plasmid_injector"));

    private static BlockBehaviour.Properties settings(String name) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, name)))
                .mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0f, 6.0f).sound(SoundType.METAL);
    }

    public static void registerBlocks() {
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "cell_analyzer"), CELL_ANALYZER);
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_extractor"), DNA_EXTRACTOR);
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_decrypter"), DNA_DECRYPTER);
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_infuser"), PLASMID_INFUSER);
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "blood_purifier"), BLOOD_PURIFIER);
        Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_injector"), PLASMID_INJECTOR);
    }
}
