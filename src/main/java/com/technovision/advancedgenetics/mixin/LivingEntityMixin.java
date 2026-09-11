package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import com.technovision.advancedgenetics.util.SpiderClimbUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(at = @At("RETURN"), method = "onClimbable", cancellable = true)
    public void doSpiderClimbing(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            if ((Entity)this instanceof Player player) {
                if (ComponentRegistry.PLAYER_GENETICS.get(player).hasGene(Genes.CLIMB_WALLS)) {
                    BlockPos blockPos = this.blockPosition();
                    BlockState blockBelowPlayer = level().getBlockState(blockPos.below());
                    if (blockBelowPlayer.getBlock() != Blocks.AIR
                        && !SpiderClimbUtil.isReplaceablePlant(blockBelowPlayer)
                        && !blockBelowPlayer.is(BlockTags.FLOWERS)) {
                        if (SpiderClimbUtil.canStartClimb(player, blockPos)) {
                            this.lastClimbablePos = Optional.of(blockPos);
                            info.setReturnValue(true);
                        }
                    } else if (SpiderClimbUtil.canContinueClimb(player, blockPos)) {
                        this.lastClimbablePos = Optional.of(blockPos);
                        info.setReturnValue(true);
                    }
                }
            }
        }
    }
}
