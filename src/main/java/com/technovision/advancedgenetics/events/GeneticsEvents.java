package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles genes that trigger on rick clicking an item or block.
 *
 * @author TechnoVision
 */
public class GeneticsEvents {

    public static void registerEvents() {
        // Handles the "Eat Grass" gene
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient() || !player.getMainHandStack().isEmpty()) return ActionResult.PASS;
            BlockPos pos = hitResult.getBlockPos();
            if (world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) return ActionResult.PASS;
            if (!player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.EAT_GRASS)) return ActionResult.PASS;
            if (player.getHungerManager().isNotFull()) {
                player.getHungerManager().add(1, 0.0f);
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
            return ActionResult.SUCCESS;
        });

        // Handles the "Milky" and "Meaty" genes
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient()) return ActionResult.PASS;
            if (entity instanceof PlayerEntity clickedPlayer) {
                // Milk player
                PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);
                ItemStack stack = player.getMainHandStack();
                if (stack.getItem() == Items.BUCKET && component.hasGene(Genes.MILKY)) {
                    player.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
                }
                // Shear porkchops off player
                if (stack.getItem() == Items.SHEARS && component.hasGene(Genes.MEATY)) {
                    clickedPlayer.dropStack(new ItemStack(Items.PORKCHOP));
                    player.getMainHandStack().damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                }
                // Shear wool off player
                if (stack.getItem() == Items.SHEARS && component.hasGene(Genes.WOOLY)) {
                    clickedPlayer.dropStack(new ItemStack(Items.WHITE_WOOL));
                    player.getMainHandStack().damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                }
            }
            return ActionResult.SUCCESS;
        });

        // Handles "Explosive Exit" and "Emerald Heart" gene
        ServerPlayerEvents.ALLOW_DEATH.register((player, damageSource, damageAmount) -> {
            PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);
            if (component.hasGene(Genes.EXPLOSIVE_EXIT)) {
                // Explode on death
                if (player.getInventory().count(Items.GUNPOWDER) >= 5) {
                    player.getWorld().createExplosion(player, player.getX(), player.getY(), player.getZ(), 3.0f, Explosion.DestructionType.BREAK);
                }
            }
            if (component.hasGene(Genes.EMERALD_HEART)) {
                // Drop emerald on death
                player.dropStack(new ItemStack(Items.EMERALD));
            }
            return true;
        });

        // Handles "Wither Hit" gene
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.WITHER_HIT)) {
                // Apply wither affect for 1-5 seconds
                if (entity instanceof LivingEntity livingEntity) {
                    if (!livingEntity.hasStatusEffect(StatusEffects.WITHER)) {
                        int seconds = ThreadLocalRandom.current().nextInt(5) + 1;
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20*seconds, 0));
                        return ActionResult.SUCCESS;
                    }
                }
            }
            return ActionResult.PASS;
        });

        // Handles "Shoot Fireball" gene
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.SHOOT_FIREBALLS)) {
                // Shoots a fire charge if holding blaze rod
                if (player.getMainHandStack().getItem() == Items.BLAZE_ROD) {
                    // TODO: FIX THIS VECTOR!
                    Vec3d v3 = player.getCameraPosVec(0);
                    FireballEntity fireballEntity = new FireballEntity(world, player, v3.getX(), v3.getY(), v3.getZ(), 1);
                    world.spawnEntity(fireballEntity);
                    player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);
                    return TypedActionResult.success(player.getMainHandStack());
                }
            }
            return TypedActionResult.pass(player.getMainHandStack());
        });
    }
}
