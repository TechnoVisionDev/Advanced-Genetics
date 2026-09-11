package com.technovision.advancedgenetics.common.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireballEntity extends SmallFireball {
    public FireballEntity(Level level, Player player, Vec3 direction) {
        super(level, player.getX(), player.getY() + player.getEyeHeight(), player.getZ(), direction);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (level() instanceof ServerLevel serverLevel) {
            boolean griefing = serverLevel.getGameRules().get(GameRules.MOB_GRIEFING);
            serverLevel.explode(null, getX(), getY(), getZ(), 1, griefing,
                    griefing ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (level() instanceof ServerLevel serverLevel) {
            Entity target = entityHitResult.getEntity();
            Entity owner = getOwner();
            var source = damageSources().fireball(this, owner);
            target.hurtServer(serverLevel, source, 5.0F);
            if (owner instanceof LivingEntity) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, target, source);
            }
        }
    }
}
