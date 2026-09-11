package com.technovision.advancedgenetics.mixin;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AvoidEntityGoal.class)
public interface FleeEntityGoalAccessor {

    @Accessor
    TargetingConditions getAvoidEntityTargeting();
}
