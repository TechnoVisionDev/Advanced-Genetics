package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Entities;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.common.entity.FireballEntity;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
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
            if (world.isClient() || hand != Hand.MAIN_HAND) return ActionResult.PASS;
            if (entity instanceof PlayerEntity clickedPlayer) {
                // Milk player
                PlayerGeneticsComponent component = clickedPlayer.getComponent(ComponentRegistry.PLAYER_GENETICS);
                ItemStack stack = player.getMainHandStack();
                if (stack.getItem() == Items.BUCKET && component.hasGene(Genes.MILKY)) {
                    if (!component.isOnCooldown("milky")) {
                        player.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
                        component.addCooldown("milky", 15);
                        player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Text.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.MILKY.getName()+"§f"));
                    }
                    return ActionResult.SUCCESS;
                }
                // Get honey from player
                if (stack.getItem() == Items.GLASS_BOTTLE && component.hasGene(Genes.BEELICIOUS)) {
                    if (!component.isOnCooldown("beelicious")) {
                        player.getStackInHand(Hand.MAIN_HAND).decrement(1);
                        player.getInventory().insertStack(new ItemStack(Items.HONEY_BOTTLE));
                        component.addCooldown("beelicious", 300);
                        player.playSound(SoundEvents.BLOCK_BEEHIVE_DRIP, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Text.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.BEELICIOUS.getName()+"§f"));
                    }
                    return ActionResult.SUCCESS;
                }
                // Shear porkchops off player
                if (stack.getItem() == Items.SHEARS && component.hasGene(Genes.MEATY)) {
                    if (!component.isOnCooldown("meaty")) {
                        clickedPlayer.dropStack(new ItemStack(Items.PORKCHOP, 1));
                        player.getMainHandStack().damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                        component.addCooldown("meaty", 15);
                        player.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Text.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.MEATY.getName()+"§f"));
                    }
                    return ActionResult.SUCCESS;
                }
                // Shear wool off player
                if (stack.getItem() == Items.SHEARS && component.hasGene(Genes.WOOLY)) {
                    if (!component.isOnCooldown("wooly")) {
                        clickedPlayer.dropStack(new ItemStack(Items.WHITE_WOOL, 1));
                        player.getMainHandStack().damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                        component.addCooldown("wooly", 15);
                        player.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Text.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.WOOLY.getName()+"§f"));
                    }
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });

        // Handles "Explosive Exit", "Emerald Heart", and "Slimy" gene
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
                if (!component.isOnCooldown("emerald_heart")) {
                    player.dropStack(new ItemStack(Items.EMERALD));
                    component.addCooldown("emerald_heart", 300);
                }
            }
            if (component.hasGene(Genes.SLIMY)) {
                // Spawn slime
                SlimeEntity slime = new SlimeEntity(EntityType.SLIME, player.getWorld());
                slime.setPosition(player.getPos());
                slime.setSize(ThreadLocalRandom.current().nextInt(3), false);
                player.getWorld().spawnEntity(slime);
            }
            return true;
        });

        // Handles "Wither Hit" and "Venom" gene
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
            PlayerGeneticsComponent component = player.getComponent(ComponentRegistry.PLAYER_GENETICS);
            if (entity instanceof LivingEntity livingEntity) {
                if (component.hasGene(Genes.WITHER_HIT) && !livingEntity.hasStatusEffect(StatusEffects.WITHER)) {
                    // Apply wither affect for 1-5 seconds
                    int seconds = ThreadLocalRandom.current().nextInt(5) + 1;
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20*seconds, 0));
                    return ActionResult.SUCCESS;
                }
                if (component.hasGene(Genes.VENOM) && !livingEntity.hasStatusEffect(StatusEffects.POISON)) {
                    // 5% chance to apply poison affect for 30 seconds
                    if (ThreadLocalRandom.current().nextDouble() <= 0.05) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 30, 0));
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
                if (player.getMainHandStack().getItem() == Items.FIRE_CHARGE) {
                    if (!player.isCreative()) player.getMainHandStack().decrement(1);
                    Vec3d v3 = player.getRotationVec(1);
                    FireballEntity fireballEntity = new FireballEntity(world, player, v3);
                    world.spawnEntity(fireballEntity);
                    player.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);
                    return TypedActionResult.success(player.getMainHandStack());
                }
            }
            return TypedActionResult.pass(player.getMainHandStack());
        });

        // Handles "Keep Inventory" gene
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (newPlayer.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.KEEP_INVENTORY)) {
                newPlayer.getInventory().clone(oldPlayer.getInventory());
            }
        });
    }
}
