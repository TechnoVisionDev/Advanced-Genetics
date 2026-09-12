package com.technovision.advancedgenetics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

/**
 * Removes all genes from a specified player.
 *
 * @author TechnoVision
 */
public class ClearGeneCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext access, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("gene")
                .then(Commands.literal("clear")
                .then(Commands.argument("player", EntityArgument.player())
                .requires(source -> source.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_GAMEMASTER))
                .executes(ClearGeneCommand::run)
        )));
    }

    private static int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try {
            EntitySelector selector = context.getArgument("player", EntitySelector.class);
            Player player = selector.findSinglePlayer(context.getSource());
            ComponentRegistry.PLAYER_GENETICS.get(player).removeAllGenes();
            player.sendSystemMessage(Component.translatable("message.advancedgenetics.command.gene_clear", "§7"+player.getName().getString()+"§f"));
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
