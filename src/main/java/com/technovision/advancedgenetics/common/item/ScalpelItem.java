package com.technovision.advancedgenetics.common.item;

import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

public class ScalpelItem extends Item {
    public ScalpelItem(Properties properties, int durability) { super(properties.durability(durability)); }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        ItemCooldowns cooldowns = user.getCooldowns();
        if (user.level() instanceof ServerLevel level && hand == InteractionHand.MAIN_HAND && !cooldowns.isOnCooldown(stack)) {
            scrapeEntity(level, stack, user, entity);
            cooldowns.addCooldown(stack, 10);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void scrapeEntity(ServerLevel level, ItemStack stack, Player user, LivingEntity entity) {
        EntityType<?> type = entity.getType();
        OrganicMatterItem item = ItemRegistry.MATTER.get(type);
        if (item == null) {
            if (type == EntityType.SKELETON_HORSE) item = ItemRegistry.MATTER.get(EntityType.SKELETON);
            else if (type == EntityType.ZOMBIE_HORSE || type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.ZOMBIE_VILLAGER)
                item = ItemRegistry.MATTER.get(EntityType.ZOMBIE);
            else if (type == EntityType.MULE) item = ItemRegistry.MATTER.get(EntityType.HORSE);
            else if (type == EntityType.PIGLIN || type == EntityType.PIGLIN_BRUTE) item = ItemRegistry.MATTER.get(EntityType.PIG);
            else if (type == EntityType.WITCH) item = ItemRegistry.MATTER.get(EntityType.VILLAGER);
        }
        if (item != null) {
            entity.spawnAtLocation(level, item);
            entity.hurtServer(level, user.damageSources().playerAttack(user), 1.0F);
            stack.hurtAndBreak(1, user, InteractionHand.MAIN_HAND);
        }
    }
}
