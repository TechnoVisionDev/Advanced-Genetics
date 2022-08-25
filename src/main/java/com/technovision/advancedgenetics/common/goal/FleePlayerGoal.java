package com.technovision.advancedgenetics.common.goal;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.mixin.FleeEntityGoalAccessor;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Flee from players that have a certain gene
 */
public class FleePlayerGoal extends FleeEntityGoal<PlayerEntity> {

    private final Genes requiredGene;

    public FleePlayerGoal(PathAwareEntity mob, Genes requiredGene, float distance, double slowSpeed, double fastSpeed) {
        super(mob, PlayerEntity.class, distance, slowSpeed, fastSpeed);
        this.requiredGene = requiredGene;
    }

    @Override
    public boolean canStart() {
        this.targetEntity = this.mob.world.getClosestEntity(this.mob.world.getEntitiesByClass(this.classToFleeFrom, this.mob.getBoundingBox().expand((double)this.fleeDistance, 3.0, (double)this.fleeDistance), (livingEntity) -> {
            return true;
        }), ((FleeEntityGoalAccessor) this).getWithinRangePredicate(), this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
        if (this.targetEntity == null || !targetEntity.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(requiredGene)) {
            return false;
        } else {
            Vec3d vec3d = NoPenaltyTargeting.findFrom(this.mob, 16, 7, this.targetEntity.getPos());
            if (vec3d == null) {
                return false;
            } else if (this.targetEntity.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z) < this.targetEntity.squaredDistanceTo(this.mob)) {
                return false;
            } else {
                this.fleePath = this.fleeingEntityNavigation.findPathTo(vec3d.x, vec3d.y, vec3d.z, 0);
                return this.fleePath != null;
            }
        }
    }
}
