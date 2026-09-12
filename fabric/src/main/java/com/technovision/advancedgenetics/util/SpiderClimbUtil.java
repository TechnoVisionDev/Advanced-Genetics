package com.technovision.advancedgenetics.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class SpiderClimbUtil {
    private SpiderClimbUtil() { }

    /** The original replaceable_plants tag was removed; retain its climbing exclusions. */
    public static boolean isReplaceablePlant(BlockState state) {
        return state.is(Blocks.SHORT_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.DEAD_BUSH)
                || state.is(Blocks.VINE) || state.is(Blocks.GLOW_LICHEN) || state.is(Blocks.SUNFLOWER)
                || state.is(Blocks.LILAC) || state.is(Blocks.ROSE_BUSH) || state.is(Blocks.PEONY)
                || state.is(Blocks.TALL_GRASS) || state.is(Blocks.LARGE_FERN) || state.is(Blocks.HANGING_ROOTS);
    }

    public static boolean canStartClimb(Player player, BlockPos blockPos) {
        Level level = player.level();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos adjacent = blockPos.relative(direction);
            BlockState lower = level.getBlockState(adjacent);
            BlockState upper = level.getBlockState(adjacent.above());
            if (!lower.is(Blocks.AIR) && !upper.is(Blocks.AIR)
                    && !isReplaceablePlant(lower) && !isReplaceablePlant(upper)) return true;
        }
        return false;
    }

    public static boolean canContinueClimb(Player player, BlockPos blockPos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState state = player.level().getBlockState(blockPos.relative(direction));
            if (!state.is(Blocks.AIR) && !isReplaceablePlant(state)) return true;
        }
        return false;
    }
}
