package com.technovision.advancedgenetics.common.goal;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.mixin.FleeEntityGoalAccessor;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/** Flee from the nearest player when that player has the specified gene. */
public class FleePlayerGoal extends AvoidEntityGoal<Player> {
    private final Genes requiredGene;

    public FleePlayerGoal(PathfinderMob mob, Genes requiredGene, float distance, double slowSpeed, double fastSpeed) {
        super(mob, Player.class, distance, slowSpeed, fastSpeed);
        this.requiredGene = requiredGene;
    }

    @Override
    public boolean canUse() {
        ServerLevel level = (ServerLevel) mob.level();
        toAvoid = level.getNearestEntity(level.getEntitiesOfClass(avoidClass, mob.getBoundingBox().inflate(maxDist, 3.0, maxDist), entity -> true),
                ((FleeEntityGoalAccessor) this).getAvoidEntityTargeting(), mob, mob.getX(), mob.getY(), mob.getZ());
        if (toAvoid == null || !ComponentRegistry.PLAYER_GENETICS.get(toAvoid).hasGene(requiredGene)) return false;
        Vec3 target = DefaultRandomPos.getPosAway(mob, 16, 7, toAvoid.position());
        if (target == null || toAvoid.distanceToSqr(target.x, target.y, target.z) < toAvoid.distanceToSqr(mob)) return false;
        path = pathNav.createPath(target.x, target.y, target.z, 0);
        return path != null;
    }
}
