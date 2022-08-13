package com.technovision.advancedgenetics.registry;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.common.item.CellItem;
import com.technovision.advancedgenetics.common.item.OrganicMatterItem;
import com.technovision.advancedgenetics.common.item.ScalpelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRegistry {

    private static final FabricItemSettings ITEM_SETTINGS = new FabricItemSettings().group(AdvancedGenetics.TAB);

    public static final Map<String, OrganicMatterItem> MATTER = new HashMap<>();
    public static final List<CellItem> CELLS = new ArrayList<>();

    // Tools
    public static final ScalpelItem METAL_SCALPEL = new ScalpelItem(25);
    public static final ScalpelItem DIAMOND_SCALPEL = new ScalpelItem(150);
    public static final ScalpelItem NETHERITE_SCALPEL = new ScalpelItem(300);
    public static final Item GLASS_SYRINGE = new Item(ITEM_SETTINGS);
    public static final Item OVERCLOCKER = new Item(ITEM_SETTINGS);

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

        // Organic Matter
        for (Entities entity : Entities.values()) {
            String key = entity.getName() + "_matter";
            OrganicMatterItem matterItem = new OrganicMatterItem(EntityType.COW);
            Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, key), matterItem);
            MATTER.put(entity.getType().getName().getString(), matterItem);
        }

        // Cells
        for (Entities entity : Entities.values()) {
            String key = entity.getName() + "_cell";
            CellItem cellItem = new CellItem(EntityType.COW, entity.getColor());
            Registry.register(Registry.ITEM, new Identifier(AdvancedGenetics.MOD_ID, key), cellItem);
            CELLS.add(cellItem);
        }
    }
}
