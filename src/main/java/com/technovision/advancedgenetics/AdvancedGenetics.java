package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.client.ClientEvents;
import com.technovision.advancedgenetics.commands.AddGeneCommand;
import com.technovision.advancedgenetics.commands.ClearGeneCommand;
import com.technovision.advancedgenetics.commands.GeneArgumentType;
import com.technovision.advancedgenetics.commands.RemoveGeneCommand;
import com.technovision.advancedgenetics.events.GeneticsEvents;
import com.technovision.advancedgenetics.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class AdvancedGenetics implements ModInitializer {

    public static final String MOD_ID = "advancedgenetics";

    public static final ItemGroup TAB = FabricItemGroupBuilder.build(
            new Identifier(MOD_ID, "tab"),
            () -> new ItemStack(ItemRegistry.CELL_ANALYZER)
    );

    @Override
    public void onInitialize() {
        // Register and load config
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, Config.COMMON_SPEC, "AdvancedGenetics.toml");

        // Register in-game items, blocks, entities, and GUIs
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        BlockEntityRegistry.registerBlockEntities();
        ScreenRegistry.registerScreens();
        RecipeRegistry.registerRecipes();
        ClientEvents.propertyOverrideRegistry();

        // Register events
        GeneticsEvents.registerEvents();

        // Register commands
        ArgumentTypeRegistry.registerArgumentType(new Identifier(MOD_ID, "gene"), GeneArgumentType.class, ConstantArgumentSerializer.of(GeneArgumentType::gene));
        CommandRegistrationCallback.EVENT.register(AddGeneCommand::register);
        CommandRegistrationCallback.EVENT.register(RemoveGeneCommand::register);
        CommandRegistrationCallback.EVENT.register(ClearGeneCommand::register);
    }
}
