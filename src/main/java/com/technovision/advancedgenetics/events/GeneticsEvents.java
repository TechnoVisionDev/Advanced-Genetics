package com.technovision.advancedgenetics.events;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.common.entity.FireballEntity;
import com.technovision.advancedgenetics.component.PlayerGeneticsComponent;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.ThreadLocalRandom;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;


/**
 * Handles genes that trigger on rick clicking an item or block.
 *
 * @author TechnoVision
 */
public class GeneticsEvents {

    public static void registerEvents() {
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            var result = useBlock(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
            if (result != InteractionResult.PASS) { event.setCancellationResult(result); event.setCanceled(true); }
        });
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.EntityInteract event) -> {
            var result = useEntity(event.getEntity(), event.getLevel(), event.getHand(), event.getTarget());
            if (result != InteractionResult.PASS) { event.setCancellationResult(result); event.setCanceled(true); }
        });
        NeoForge.EVENT_BUS.addListener((PlayerInteractEvent.RightClickItem event) -> {
            var result = useItem(event.getEntity(), event.getLevel(), event.getHand());
            if (result != InteractionResult.PASS) { event.setCancellationResult(result); event.setCanceled(true); }
        });
        NeoForge.EVENT_BUS.addListener((AttackEntityEvent event) -> {
            if (attack(event.getEntity(), event.getEntity().level(), event.getTarget()) != InteractionResult.PASS) event.setCanceled(true);
        });
        NeoForge.EVENT_BUS.addListener((LivingDeathEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) onDeath(player);
        });
        NeoForge.EVENT_BUS.addListener((PlayerEvent.Clone event) -> {
            if (ComponentRegistry.PLAYER_GENETICS.get(event.getOriginal()).hasGene(Genes.KEEP_INVENTORY)) {
                event.getEntity().getInventory().replaceWith(event.getOriginal().getInventory());
            }
        });
        NeoForge.EVENT_BUS.addListener((PlayerTickEvent.Post event) -> {
            if (!event.getEntity().level().isClientSide()) ComponentRegistry.PLAYER_GENETICS.get(event.getEntity()).serverTick();
        });
    }

    public static InteractionResult useBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
            if (world.isClientSide() || !player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
            BlockPos pos = hitResult.getBlockPos();
            if (world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) return InteractionResult.PASS;
            if (!ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.EAT_GRASS)) return InteractionResult.PASS;
            if (player.getFoodData().needsFood()) {
                player.getFoodData().eat(1, 0.0f);
                world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
            return InteractionResult.SUCCESS;
    }

    public static InteractionResult useEntity(Player player, Level world, InteractionHand hand, Entity entity) {
            if (world.isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
            boolean handled = false;
            if (entity instanceof Player clickedPlayer) {
                // Milk player
                PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(clickedPlayer);
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() == Items.BUCKET && component.hasGene(Genes.MILKY)) {
                    handled = true;
                    if (!component.isOnCooldown("milky")) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.MILK_BUCKET));
                        component.addCooldown("milky", 15);
                        player.playSound(SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
                    } else {
                        player.sendSystemMessage(Component.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.MILKY.getName()+"§f"));
                    }
                }
                // Get honey from player
                if (stack.getItem() == Items.GLASS_BOTTLE && component.hasGene(Genes.BEELICIOUS)) {
                    handled = true;
                    if (!component.isOnCooldown("beelicious")) {
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        player.getInventory().add(new ItemStack(Items.HONEY_BOTTLE));
                        component.addCooldown("beelicious", 300);
                        player.playSound(SoundEvents.BEEHIVE_DRIP, 1.0f, 1.0f);
                    } else {
                        player.sendSystemMessage(Component.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.BEELICIOUS.getName()+"§f"));
                    }
                }
                // Shear porkchops off player
                if (stack.getItem() == Items.SHEARS && component.hasGene(Genes.MEATY)) {
                    handled = true;
                    if (!component.isOnCooldown("meaty")) {
                        clickedPlayer.spawnAtLocation((ServerLevel) world, new ItemStack(Items.PORKCHOP, 1));
                        player.getMainHandItem().hurtAndBreak(1, player, player.getUsedItemHand());
                        component.addCooldown("meaty", 15);
                        player.playSound(SoundEvents.MOOSHROOM_SHEAR, 1.0f, 1.0f);
                    } else {
                        player.sendSystemMessage(Component.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.MEATY.getName()+"§f"));
                    }
                }
                // Shear wool off player
                if (stack.getItem() == Items.SHEARS && component.hasGene(Genes.WOOLY)) {
                    handled = true;
                    if (!component.isOnCooldown("wooly")) {
                        clickedPlayer.spawnAtLocation((ServerLevel) world, new ItemStack(Items.WHITE_WOOL, 1));
                        player.getMainHandItem().hurtAndBreak(1, player, player.getUsedItemHand());
                        component.addCooldown("wooly", 15);
                        player.playSound(SoundEvents.SHEEP_SHEAR, 1.0f, 1.0f);
                    } else {
                        player.sendSystemMessage(Component.translatable("message."+ AdvancedGenetics.MOD_ID+".cooldown", "§7"+Genes.WOOLY.getName()+"§f"));
                    }
                }
            }
            // Leave unrelated entity clicks available to scalpels and vanilla interactions.
            return handled ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    public static void onDeath(ServerPlayer player) {
            PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(player);
            if (component.hasGene(Genes.EXPLOSIVE_EXIT)) {
                // Explode on death
                if (player.getInventory().countItem(Items.GUNPOWDER) >= 5) {
                    player.level().explode(player, player.getX(), player.getY(), player.getZ(), 3.0f, Level.ExplosionInteraction.BLOCK);
                }
            }
            if (component.hasGene(Genes.EMERALD_HEART)) {
                // Drop emerald on death
                if (!component.isOnCooldown("emerald_heart")) {
                    player.spawnAtLocation(player.level(), new ItemStack(Items.EMERALD));
                    component.addCooldown("emerald_heart", 300);
                }
            }
            if (component.hasGene(Genes.SLIMY)) {
                // Spawn slime
                Slime slime = new Slime(EntityType.SLIME, player.level());
                slime.setPos(player.position());
                slime.setSize(ThreadLocalRandom.current().nextInt(3), false);
                player.level().addFreshEntity(slime);
            }
    }

    public static InteractionResult attack(Player player, Level world, Entity entity) {
            if (world.isClientSide()) return InteractionResult.PASS;
            if (ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.WITHER_HIT)) {
                // Apply wither affect for 1-5 seconds
                if (entity instanceof LivingEntity livingEntity) {
                    if (!livingEntity.hasEffect(MobEffects.WITHER)) {
                        int seconds = ThreadLocalRandom.current().nextInt(5) + 1;
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20*seconds, 0));
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            PlayerGeneticsComponent component = ComponentRegistry.PLAYER_GENETICS.get(player);
            if (entity instanceof LivingEntity livingEntity) {
                if (component.hasGene(Genes.WITHER_HIT) && !livingEntity.hasEffect(MobEffects.WITHER)) {
                    // Apply wither affect for 1-5 seconds
                    int seconds = ThreadLocalRandom.current().nextInt(5) + 1;
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20*seconds, 0));
                    return InteractionResult.SUCCESS;
                }
                if (component.hasGene(Genes.VENOM) && !livingEntity.hasEffect(MobEffects.POISON)) {
                    // 5% chance to apply poison affect for 30 seconds
                    if (ThreadLocalRandom.current().nextDouble() <= 0.05) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 30, 0));
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.PASS;
    }

    public static InteractionResult useItem(Player player, Level world, InteractionHand hand) {
            if (world.isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
            if (!ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.SHOOT_FIREBALLS)) {
                // Shoots a fire charge if holding blaze rod
                if (player.getMainHandItem().getItem() == Items.FIRE_CHARGE) {
                    if (!player.isCreative()) player.getMainHandItem().shrink(1);
                    Vec3 v3 = player.getViewVector(1);
                    FireballEntity fireballEntity = new FireballEntity(world, player, v3);
                    world.addFreshEntity(fireballEntity);
                    player.playSound(SoundEvents.FIRECHARGE_USE, 1.0f, 1.0f);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
    }
}
