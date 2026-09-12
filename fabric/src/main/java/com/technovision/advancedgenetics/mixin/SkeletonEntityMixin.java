package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.common.goal.FleePlayerGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public class SkeletonEntityMixin extends Mob {

    protected SkeletonEntityMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    /**
     * Adds goal to flee from players with 'Scare Skeletons' gene
     */
    @Inject(method = "registerGoals", at = @At("HEAD"))
    protected void initGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new FleePlayerGoal(((AbstractSkeleton)(Object)this), Genes.SCARE_SKELETONS, 10.0F, 1.0, 1.2));
    }
}
