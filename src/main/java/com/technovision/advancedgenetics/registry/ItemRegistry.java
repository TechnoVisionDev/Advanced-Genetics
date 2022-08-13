package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.common.item.OrganicMatterItem;
import com.technovision.advancedgenetics.common.item.ScalpelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {

    private static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(AdvancedGenetics.TAB);

    // Items
    public static final ScalpelItem METAL_SCALPEL = new ScalpelItem(25);
    public static final ScalpelItem DIAMOND_SCALPEL = new ScalpelItem(150);
    public static final OrganicMatterItem COW_MATTER = new OrganicMatterItem(EntityType.COW);
    public static final OrganicMatterItem PIG_MATTER = new OrganicMatterItem(EntityType.PIG);

    // Block Items
    public static final BlockItem CELL_ANALYZER = new BlockItem(BlockRegistry.CELL_ANALYZER, ITEM_SETTINGS);

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "cell_analyzer"), CELL_ANALYZER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "metal_scalpel"), METAL_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "diamond_scalpel"), DIAMOND_SCALPEL);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "cow_matter"), COW_MATTER);
        Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, "pig_matter"), PIG_MATTER);
    }
}
