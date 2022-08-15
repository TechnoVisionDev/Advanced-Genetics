package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.common.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(AdvancedGenetics.TAB);

    // Organic Matter and Cells
    public static final Map<EntityType, OrganicMatterItem> MATTER = new HashMap<>();
    public static final Map<EntityType, CellItem> CELLS = new HashMap<>();

    // Tools
    public static final ScalpelItem METAL_SCALPEL = new ScalpelItem(25);
    public static final ScalpelItem DIAMOND_SCALPEL = new ScalpelItem(150);
    public static final ScalpelItem NETHERITE_SCALPEL = new ScalpelItem(300);
    public static final SyringeItem GLASS_SYRINGE = new SyringeItem();
    public static final Item OVERCLOCKER = new Item(ITEM_SETTINGS);

    // Block Items
    public static final BlockItem CELL_ANALYZER = new BlockItem(BlockRegistry.CELL_ANALYZER, ITEM_SETTINGS);
    public static final BlockItem DNA_EXTRACTOR = new BlockItem(BlockRegistry.DNA_EXTRACTOR, ITEM_SETTINGS);
    public static final BlockItem DNA_DECRYPTER = new BlockItem(BlockRegistry.DNA_DECRYPTER, ITEM_SETTINGS);
    public static final BlockItem PLASMID_INFUSER = new BlockItem(BlockRegistry.PLASMID_INFUSER, ITEM_SETTINGS);
    public static final BlockItem BLOOD_PURIFIER = new BlockItem(BlockRegistry.BLOOD_PURIFIER, ITEM_SETTINGS);
    public static final BlockItem PLASMID_INJECTOR = new BlockItem(BlockRegistry.PLASMID_INJECTOR, ITEM_SETTINGS);

    // Other Items
    public static final DnaItem DNA_HELIX = new DnaItem();
    public static final PlasmidItem PLASMID = new PlasmidItem();
    public static final PlasmidItem ANTIPLASMID = new PlasmidItem();

    public static void registerItems() {
        // Block Items
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer"), CELL_ANALYZER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "dna_extractor"), DNA_EXTRACTOR);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "dna_decrypter"), DNA_DECRYPTER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "plasmid_infuser"), PLASMID_INFUSER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "blood_purifier"), BLOOD_PURIFIER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "plasmid_injector"), PLASMID_INJECTOR);

        // Tools
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "metal_scalpel"), METAL_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "diamond_scalpel"), DIAMOND_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "netherite_scalpel"), NETHERITE_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "glass_syringe"), GLASS_SYRINGE);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "overclocker"), OVERCLOCKER);

        // Organic Matter
        for (Entities entity : Entities.values()) {
            String key = entity.getName() + "_matter";
            OrganicMatterItem matterItem = new OrganicMatterItem(entity.getType());
            Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, key), matterItem);
            MATTER.put(entity.getType(), matterItem);
        }

        // Cells
        for (Entities entity : Entities.values()) {
            String key = entity.getName() + "_cell";
            CellItem cellItem = new CellItem(entity.getType(), entity.getColor());
            Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, key), cellItem);
            CELLS.put(entity.getType(), cellItem);
        }

        // Other Items
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "dna_helix"), DNA_HELIX);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "plasmid"), PLASMID);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "antiplasmid"), ANTIPLASMID);
    }
}
