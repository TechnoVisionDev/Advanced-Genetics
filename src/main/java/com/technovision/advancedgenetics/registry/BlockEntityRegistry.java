package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.block.bloodpurifier.BloodPurifierBlockEntity;
import com.technovision.advancedgenetics.common.block.cellanalyzer.CellAnalyzerBlockEntity;
import com.technovision.advancedgenetics.common.block.dnadecrypter.DnaDecrypterBlockEntity;
import com.technovision.advancedgenetics.common.block.dnaextractor.DnaExtractorBlockEntity;
import com.technovision.advancedgenetics.common.block.plasmidinfuser.PlasmidInfuserBlockEntity;
import com.technovision.advancedgenetics.common.block.plasmidinjector.PlasmidInjectorBlockEntity;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;


public class BlockEntityRegistry {

    public static final BlockEntityType<CellAnalyzerBlockEntity> CELL_ANALYZER_BLOCK_ENTITY = new BlockEntityType<>(CellAnalyzerBlockEntity::new, BlockRegistry.CELL_ANALYZER);
    public static final BlockEntityType<DnaExtractorBlockEntity> DNA_EXTRACTOR_BLOCK_ENTITY = new BlockEntityType<>(DnaExtractorBlockEntity::new, BlockRegistry.DNA_EXTRACTOR);
    public static final BlockEntityType<DnaDecrypterBlockEntity> DNA_DECRYPTER_BLOCK_ENTITY = new BlockEntityType<>(DnaDecrypterBlockEntity::new, BlockRegistry.DNA_DECRYPTER);
    public static final BlockEntityType<PlasmidInfuserBlockEntity> PLASMID_INFUSER_BLOCK_ENTITY = new BlockEntityType<>(PlasmidInfuserBlockEntity::new, BlockRegistry.PLASMID_INFUSER);
    public static final BlockEntityType<BloodPurifierBlockEntity> BLOOD_PURIFIER_BLOCK_ENTITY = new BlockEntityType<>(BloodPurifierBlockEntity::new, BlockRegistry.BLOOD_PURIFIER);
    public static final BlockEntityType<PlasmidInjectorBlockEntity> PLASMID_INJECTOR_BLOCK_ENTITY = new BlockEntityType<>(PlasmidInjectorBlockEntity::new, BlockRegistry.PLASMID_INJECTOR);

    public static void registerBlockEntities() {
        // Register block entity
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "cell_analyzer_block_entity"), CELL_ANALYZER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_extractor_block_entity"), DNA_EXTRACTOR_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "dna_decrypter_block_entity"), DNA_DECRYPTER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_infuser_block_entity"), PLASMID_INFUSER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "blood_purifier_block_entity"), BLOOD_PURIFIER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, "plasmid_injector_block_entity"), PLASMID_INJECTOR_BLOCK_ENTITY);

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Energy.BLOCK, CELL_ANALYZER_BLOCK_ENTITY, (machine, side) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Item.BLOCK, CELL_ANALYZER_BLOCK_ENTITY, (machine, side) -> new WorldlyContainerWrapper(machine, side));
        event.registerBlockEntity(Capabilities.Energy.BLOCK, DNA_EXTRACTOR_BLOCK_ENTITY, (machine, side) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Item.BLOCK, DNA_EXTRACTOR_BLOCK_ENTITY, (machine, side) -> new WorldlyContainerWrapper(machine, side));
        event.registerBlockEntity(Capabilities.Energy.BLOCK, DNA_DECRYPTER_BLOCK_ENTITY, (machine, side) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Item.BLOCK, DNA_DECRYPTER_BLOCK_ENTITY, (machine, side) -> new WorldlyContainerWrapper(machine, side));
        event.registerBlockEntity(Capabilities.Energy.BLOCK, PLASMID_INFUSER_BLOCK_ENTITY, (machine, side) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Item.BLOCK, PLASMID_INFUSER_BLOCK_ENTITY, (machine, side) -> new WorldlyContainerWrapper(machine, side));
        event.registerBlockEntity(Capabilities.Energy.BLOCK, BLOOD_PURIFIER_BLOCK_ENTITY, (machine, side) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Item.BLOCK, BLOOD_PURIFIER_BLOCK_ENTITY, (machine, side) -> new WorldlyContainerWrapper(machine, side));
        event.registerBlockEntity(Capabilities.Energy.BLOCK, PLASMID_INJECTOR_BLOCK_ENTITY, (machine, side) -> machine.getEnergyStorage());
        event.registerBlockEntity(Capabilities.Item.BLOCK, PLASMID_INJECTOR_BLOCK_ENTITY, (machine, side) -> new WorldlyContainerWrapper(machine, side));
    }
}
