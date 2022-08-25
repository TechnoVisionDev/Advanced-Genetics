package com.technovision.advancedgenetics.mixin;

import com.technovision.advancedgenetics.api.genetics.Genes;
import com.technovision.advancedgenetics.registry.ComponentRegistry;
import com.technovision.advancedgenetics.util.SpiderClimbUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    private Optional<BlockPos> climbingPos;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("RETURN"), method = "isClimbing", cancellable = true)
    public void doSpiderClimbing(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            if ((Entity)this instanceof PlayerEntity player) {
                if (player.getComponent(ComponentRegistry.PLAYER_GENETICS).hasGene(Genes.CLIMB_WALLS)) {
                    BlockPos blockPos = this.getBlockPos();
                    BlockState blockBelowPlayer = world.getBlockState(blockPos.offset(Direction.DOWN, 1));
                    if (blockBelowPlayer.getBlock() != Blocks.AIR
                        && !blockBelowPlayer.isIn(BlockTags.REPLACEABLE_PLANTS)
                        && !blockBelowPlayer.isIn(BlockTags.FLOWERS)) {
                        if (SpiderClimbUtil.canStartClimb(player, blockPos)) {
                            this.climbingPos = Optional.of(blockPos);
                            info.setReturnValue(true);
                        }
                    } else if (SpiderClimbUtil.canContinueClimb(player, blockPos)) {
                        this.climbingPos = Optional.of(blockPos);
                        info.setReturnValue(true);
                    }
                }
            }
        }
    }
}
