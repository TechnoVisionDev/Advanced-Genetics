package com.technovision.advancedgenetics.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/**
 * Removes a gene to a specified player.
 *
 * @author TechnoVision
 */
public class RemoveGeneCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("gene")
                .then(CommandManager.literal("remove")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                .then(CommandManager.argument("gene", GeneArgumentType.gene())
                .requires(source -> source.hasPermissionLevel(2))
                .executes(RemoveGeneCommand::run)
        ))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            EntitySelector selector = context.getArgument("player", EntitySelector.class);
            Genes gene = context.getArgument("gene", Genes.class);
            ServerPlayerEntity player = selector.getPlayer(context.getSource());
            player.getComponent(ComponentRegistry.PLAYER_GENETICS).removeGene(gene);
            player.sendMessage(Text.literal("Removed the §7" + gene.getName() + "§f gene from " + player.getName().getString() + "."));
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
