package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.components.AdvancedGeneticsComponents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

/**
 * Handles genes that trigger on rick clicking an item or block.
 *
 * @author TechnoVision
 */
public class RightClickEvents {

    public static void registerEvents() {
        // Handles the "Eat Grass" gene
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
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

        // Handles the "Milky" and "Meaty" genes
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                    if (world.isClient()) return ActionResult.PASS;
                    if (entity instanceof PlayerEntity clickedPlayer) {
                        // Milk player
                        ItemStack stack = player.getMainHandStack();
                        if (stack.getItem() == Items.BUCKET && clickedPlayer.getComponent(AdvancedGeneticsComponents.PLAYER_GENETICS).containsGene(Genes.MILKY)) {
                            player.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
                        }
                        // Shear porkchops off player
                        if (stack.getItem() == Items.SHEARS && clickedPlayer.getComponent(AdvancedGeneticsComponents.PLAYER_GENETICS).containsGene(Genes.MEATY)) {
                            clickedPlayer.dropStack(new ItemStack(Items.PORKCHOP));
                            player.getMainHandStack().damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                        }
                        // Shear wool off player
                        if (stack.getItem() == Items.SHEARS && clickedPlayer.getComponent(AdvancedGeneticsComponents.PLAYER_GENETICS).containsGene(Genes.WOOLY)) {
                            clickedPlayer.dropStack(new ItemStack(Items.WHITE_WOOL));
                            player.getMainHandStack().damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                        }
                    }
                    return ActionResult.SUCCESS;
                }
        );
    }
}
