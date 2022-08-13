package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.item.CellItem;
import com.technovision.advancedgenetics.common.item.OrganicMatterItem;
import com.technovision.advancedgenetics.common.item.ScalpelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    private static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(AdvancedGenetics.TAB);

    // Tools
    public static final ScalpelItem METAL_SCALPEL = new ScalpelItem(25);
    public static final ScalpelItem DIAMOND_SCALPEL = new ScalpelItem(150);
    public static final ScalpelItem NETHERITE_SCALPEL = new ScalpelItem(300);
    public static final Item GLASS_SYRINGE = new Item(ITEM_SETTINGS);
    public static final Item OVERCLOCKER = new Item(ITEM_SETTINGS);

    // Organic Matter
    public static final OrganicMatterItem COW_MATTER = new OrganicMatterItem(EntityType.COW);
    public static final OrganicMatterItem PIG_MATTER = new OrganicMatterItem(EntityType.PIG);
    public static final OrganicMatterItem CHICKEN_MATTER = new OrganicMatterItem(EntityType.CHICKEN);
    public static final OrganicMatterItem SHEEP_MATTER = new OrganicMatterItem(EntityType.SHEEP);
    public static final OrganicMatterItem SQUID_MATTER = new OrganicMatterItem(EntityType.SQUID);

    // Cells
    public static final CellItem COW_CELL = new CellItem(EntityType.COW, "d0bb94");
    public static final CellItem PIG_CELL = new CellItem(EntityType.PIG, "F9A195");
    public static final CellItem CHICKEN_CELL = new CellItem(EntityType.CHICKEN, "c9c5c5");
    public static final CellItem SHEEP_CELL = new CellItem(EntityType.SHEEP, "FFFFFF");
    public static final CellItem SQUID_CELL = new CellItem(EntityType.SQUID, "3581cc");

    // Block Items
    public static final BlockItem CELL_ANALYZER = new BlockItem(BlockRegistry.CELL_ANALYZER, ITEM_SETTINGS);

    public static void registerItems() {
        // Block Items
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer"), CELL_ANALYZER);

        // Tools
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "metal_scalpel"), METAL_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "diamond_scalpel"), DIAMOND_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "netherite_scalpel"), NETHERITE_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "glass_syringe"), GLASS_SYRINGE);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "overclocker"), OVERCLOCKER);

        // Matter
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "cow_matter"), COW_MATTER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "pig_matter"), PIG_MATTER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "chicken_matter"), CHICKEN_MATTER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "sheep_matter"), SHEEP_MATTER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "squid_matter"), SQUID_MATTER);

        // Cells
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "cow_cell"), COW_CELL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "pig_cell"), PIG_CELL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "chicken_cell"), CHICKEN_CELL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "sheep_cell"), SHEEP_CELL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "squid_cell"), SQUID_CELL);
    }
}
