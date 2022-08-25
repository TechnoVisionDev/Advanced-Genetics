package com.technovision.advancedgenetics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Removes all genes from a specified player.
 *
 * @author TechnoVision
 */
public class ClearGeneCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("gene")
                .then(CommandManager.literal("clear")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                .requires(source -> source.hasPermissionLevel(2))
                .executes(ClearGeneCommand::run)
        )));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            EntitySelector selector = context.getArgument("player", EntitySelector.class);
            PlayerEntity player = selector.getPlayer(context.getSource());
            player.getComponent(ComponentRegistry.PLAYER_GENETICS).removeAllGenes();
            player.sendMessage(Text.translatable("message.advancedgenetics.command.gene_clear", "§7"+player.getName().getString()+"§f"));
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
