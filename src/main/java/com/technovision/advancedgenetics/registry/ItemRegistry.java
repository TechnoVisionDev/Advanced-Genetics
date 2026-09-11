package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.common.item.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    public static final List<Item> ALL_ITEMS = new ArrayList<>();

    private static Item.Properties settings(String path) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, path)));
    }

    private static <T extends Item> T register(String path, T item) {
        Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(AdvancedGenetics.MOD_ID, path), item);
        if (item instanceof BlockItem blockItem) blockItem.registerBlocks(Item.BY_BLOCK, item);
        ALL_ITEMS.add(item);
        return item;
    }

    // Organic Matter and Cells
    public static final Map<EntityType<?>, OrganicMatterItem> MATTER = new HashMap<>();
    public static final Map<EntityType<?>, CellItem> CELLS = new HashMap<>();

    // Tools
    public static final ScalpelItem METAL_SCALPEL = new ScalpelItem(settings("metal_scalpel"), 25);
    public static final ScalpelItem DIAMOND_SCALPEL = new ScalpelItem(settings("diamond_scalpel"), 150);
    public static final ScalpelItem NETHERITE_SCALPEL = new ScalpelItem(settings("netherite_scalpel"), 300);
    public static final SyringeItem SYRINGE = new SyringeItem(settings("syringe"));
    public static final Item OVERCLOCKER = new Item(settings("overclocker"));
    public static final Item CROWBAR = new Item(settings("crowbar").durability(100));

    // Block Items
    public static final BlockItem CELL_ANALYZER = new GeneticsBlockItem(BlockRegistry.CELL_ANALYZER, settings("cell_analyzer"));
    public static final BlockItem DNA_EXTRACTOR = new GeneticsBlockItem(BlockRegistry.DNA_EXTRACTOR, settings("dna_extractor"));
    public static final BlockItem DNA_DECRYPTER = new GeneticsBlockItem(BlockRegistry.DNA_DECRYPTER, settings("dna_decrypter"));
    public static final BlockItem PLASMID_INFUSER = new GeneticsBlockItem(BlockRegistry.PLASMID_INFUSER, settings("plasmid_infuser"));
    public static final BlockItem BLOOD_PURIFIER = new GeneticsBlockItem(BlockRegistry.BLOOD_PURIFIER, settings("blood_purifier"));
    public static final BlockItem PLASMID_INJECTOR = new GeneticsBlockItem(BlockRegistry.PLASMID_INJECTOR, settings("plasmid_injector"));

    // Other Items
    public static final DnaItem DNA_HELIX = new DnaItem(settings("dna_helix"));
    public static final PlasmidItem PLASMID = new PlasmidItem(settings("plasmid"));
    public static final AntiPlasmidItem ANTIPLASMID = new AntiPlasmidItem(settings("antiplasmid"));
    public static final DragonHealthCrystalItem DRAGON_HEALTH_CRYSTAL = new DragonHealthCrystalItem(settings("dragon_health_crystal"));

    public static void registerItems() {
        // Block Items
        register("cell_analyzer", CELL_ANALYZER);
        register("dna_extractor", DNA_EXTRACTOR);
        register("dna_decrypter", DNA_DECRYPTER);
        register("plasmid_infuser", PLASMID_INFUSER);
        register("blood_purifier", BLOOD_PURIFIER);
        register("plasmid_injector", PLASMID_INJECTOR);

        // Tools
        register("metal_scalpel", METAL_SCALPEL);
        register("diamond_scalpel", DIAMOND_SCALPEL);
        register("netherite_scalpel", NETHERITE_SCALPEL);
        register("syringe", SYRINGE);
        register("overclocker", OVERCLOCKER);
        register("crowbar", CROWBAR);

        // Items
        register("dna_helix", DNA_HELIX);
        register("plasmid", PLASMID);
        register("antiplasmid", ANTIPLASMID);
        register("dragon_health_crystal", DRAGON_HEALTH_CRYSTAL);

        // Organic Matter
        for (Entities entity : Entities.values()) {
            String key = entity.getName() + "_matter";
            OrganicMatterItem matterItem = new OrganicMatterItem(settings(key), entity.getType());
            register(key, matterItem);
            MATTER.put(entity.getType(), matterItem);
        }

        // Cells
        for (Entities entity : Entities.values()) {
            String key = entity.getName() + "_cell";
            CellItem cellItem = new CellItem(settings(key), entity.getType(), entity.getColor());
            register(key, cellItem);
            CELLS.put(entity.getType(), cellItem);
        }
    }
}
