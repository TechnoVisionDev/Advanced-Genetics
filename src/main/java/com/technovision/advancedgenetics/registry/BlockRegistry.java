package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierBlock;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerBlock;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterBlock;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorBlock;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserBlock;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    public static final CellAnalyzerBlock CELL_ANALYZER = new CellAnalyzerBlock();
    public static final DnaExtractorBlock DNA_EXTRACTOR = new DnaExtractorBlock();
    public static final DnaDecrypterBlock DNA_DECRYPTER = new DnaDecrypterBlock();
    public static final PlasmidInfuserBlock PLASMID_INFUSER = new PlasmidInfuserBlock();
    public static final BloodPurifierBlock BLOOD_PURIFIER = new BloodPurifierBlock();
    public static final PlasmidInjectorBlock PLASMID_INJECTOR = new PlasmidInjectorBlock();
    public static final Block ANTI_FIELD_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL));

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer"), CELL_ANALYZER);
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "dna_extractor"), DNA_EXTRACTOR);
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "dna_decrypter"), DNA_DECRYPTER);
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "plasmid_infuser"), PLASMID_INFUSER);
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "blood_purifier"), BLOOD_PURIFIER);
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "plasmid_injector"), PLASMID_INJECTOR);
        Registry.register(Registry.BLOCK, new Identifier(AdvancedGenetics.MOD_ID, "anti_field_block"), ANTI_FIELD_BLOCK);
    }
}
