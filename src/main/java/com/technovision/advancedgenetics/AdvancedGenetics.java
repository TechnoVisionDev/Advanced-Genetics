package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.registry.BlockEntityRegistry;
import com.technovision.advancedgenetics.registry.BlockRegistry;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import com.technovision.advancedgenetics.registry.ScreenRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class AdvancedGenetics implements ModInitializer {

    public static final String MOD_ID = "advancedgenetics";

    public static final ItemGroup TAB = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "tab"),
            () -> new ItemStack(ItemRegistry.CELL_ANALYZER)
    );

    @Override
    public void onInitialize() {
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        BlockEntityRegistry.registerBlockEntities();
        ScreenRegistry.registerScreens();
    }
}
