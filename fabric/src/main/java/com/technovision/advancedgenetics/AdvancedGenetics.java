package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.commands.AddGeneCommand;
import com.technovision.advancedgenetics.commands.ClearGeneCommand;
import com.technovision.advancedgenetics.commands.GeneArgumentType;
import com.technovision.advancedgenetics.commands.RemoveGeneCommand;
import com.technovision.advancedgenetics.events.GeneticsEvents;
import com.technovision.advancedgenetics.events.KeyInputEvents;
import com.technovision.advancedgenetics.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.neoforged.fml.config.ModConfig;

public class AdvancedGenetics implements ModInitializer {

    public static final String MOD_ID = "advancedgenetics";

    public static CreativeModeTab TAB;

    @Override
    public void onInitialize() {
        // Register and load config
        ConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, Config.COMMON_SPEC, "AdvancedGenetics.toml");

        // Register in-game items, blocks, entities, and GUIs
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();
        BlockEntityRegistry.registerBlockEntities();
        ScreenRegistry.registerScreens();
        RecipeRegistry.registerRecipes();
        TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "tab"),
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.advancedgenetics.tab"))
                        .icon(() -> new ItemStack(ItemRegistry.CELL_ANALYZER))
                        .displayItems((parameters, entries) -> ItemRegistry.ALL_ITEMS.forEach(entries::accept))
                        .build());

        // Register events
        GeneticsEvents.registerEvents();
        KeyInputEvents.registerServerSide();

        // Register commands
        ArgumentTypeRegistry.registerArgumentType(Identifier.fromNamespaceAndPath(MOD_ID, "gene"), GeneArgumentType.class, SingletonArgumentInfo.contextFree(GeneArgumentType::gene));
        CommandRegistrationCallback.EVENT.register(AddGeneCommand::register);
        CommandRegistrationCallback.EVENT.register(RemoveGeneCommand::register);
        CommandRegistrationCallback.EVENT.register(ClearGeneCommand::register);
    }
}
