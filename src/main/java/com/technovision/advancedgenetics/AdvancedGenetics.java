package com.technovision.advancedgenetics;

import com.technovision.advancedgenetics.commands.*;
import com.technovision.advancedgenetics.events.*;
import com.technovision.advancedgenetics.registry.*;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;

@Mod(AdvancedGenetics.MOD_ID)
public class AdvancedGenetics {
    public static final String MOD_ID = "advancedgenetics";
    public static CreativeModeTab TAB;

    public AdvancedGenetics(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC, "AdvancedGenetics.toml");
        bus.addListener(AdvancedGenetics::register);
        bus.addListener(BlockEntityRegistry::registerCapabilities);
        bus.addListener(KeyInputEvents::registerServerSide);
        GeneticsEvents.registerEvents();
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            AddGeneCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
            RemoveGeneCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
            ClearGeneCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        });
    }

    private static void register(RegisterEvent event) {
        var key = event.getRegistryKey();
        if (key.equals(Registries.BLOCK)) BlockRegistry.registerBlocks();
        else if (key.equals(Registries.ITEM)) ItemRegistry.registerItems();
        else if (key.equals(Registries.BLOCK_ENTITY_TYPE)) BlockEntityRegistry.registerBlockEntities();
        else if (key.equals(Registries.MENU)) ScreenRegistry.registerScreens();
        else if (key.equals(Registries.RECIPE_SERIALIZER)) RecipeRegistry.registerSerializers();
        else if (key.equals(Registries.RECIPE_TYPE)) RecipeRegistry.registerTypes();
        else if (key.equals(NeoForgeRegistries.Keys.ATTACHMENT_TYPES)) ComponentRegistry.register();
        else if (key.equals(Registries.COMMAND_ARGUMENT_TYPE)) {
            Registry.register(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "gene"),
                    ArgumentTypeInfos.registerByClass(GeneArgumentType.class, SingletonArgumentInfo.contextFree(GeneArgumentType::gene)));
        } else if (key.equals(Registries.CREATIVE_MODE_TAB)) {
            TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "tab"),
                    CreativeModeTab.builder().title(Component.translatable("itemGroup.advancedgenetics.tab"))
                            .icon(() -> new ItemStack(ItemRegistry.CELL_ANALYZER))
                            .displayItems((parameters, entries) -> {
                                ItemRegistry.ALL_ITEMS.forEach(entries::accept);
                                // Patchouli adds the Field Guide via creative_tab in the book definition.
                            }).build());
        }
    }
}
