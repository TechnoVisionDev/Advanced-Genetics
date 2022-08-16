package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.components.AdvancedGeneticsComponents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Handles genes that trigger on rick clicking an item or block
 *
 * @author TechnoVision
 */
public class RightClickEvents {

    public static void registerEvents() {
        // Handles the "Eat Grass" gene
        UseBlockCallback.EVENT.register(new Identifier(AdvancedGenetics.MOD_ID, "right_click_grass"),
            (player, world, hand, hitResult) -> {
                if (world.isClient() || !player.getMainHandStack().isEmpty()) return ActionResult.PASS;
                BlockPos pos = hitResult.getBlockPos();
                if (world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) return ActionResult.PASS;
                if (!player.getComponent(AdvancedGeneticsComponents.PLAYER_GENETICS).containsGene(Genes.EAT_GRASS)) return ActionResult.PASS;
                if (player.getHungerManager().isNotFull()) {
                    player.getHungerManager().add(1, 0.0f);
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                }
                return ActionResult.SUCCESS;
            }
        );
    }
}
