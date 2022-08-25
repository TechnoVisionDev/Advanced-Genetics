package com.technovision.advancedgenetics.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SpiderClimbUtil {

    public static boolean canStartClimb(PlayerEntity player, BlockPos blockPos) {
        World world = player.getWorld();

        BlockPos north = blockPos.offset(Direction.NORTH, 1);
        BlockPos east = blockPos.offset(Direction.EAST, 1);
        BlockPos south = blockPos.offset(Direction.SOUTH, 1);
        BlockPos west = blockPos.offset(Direction.WEST, 1);

        BlockState northBS = world.getBlockState(north);
        BlockState eastBS = world.getBlockState(east);
        BlockState southBS = world.getBlockState(south);
        BlockState westBS = world.getBlockState(west);

        BlockState northUp = world.getBlockState(north.offset(Direction.UP, 1));
        BlockState eastUp = world.getBlockState(east.offset(Direction.UP, 1));
        BlockState southUp = world.getBlockState(south.offset(Direction.UP, 1));
        BlockState westUp = world.getBlockState(west.offset(Direction.UP, 1));

        if (northBS.getBlock() != Blocks.AIR && northUp.getBlock() != Blocks.AIR) {
            return !northBS.isIn(BlockTags.REPLACEABLE_PLANTS) && !northUp.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        else if (eastBS.getBlock() != Blocks.AIR && eastUp.getBlock() != Blocks.AIR) {
            return !eastBS.isIn(BlockTags.REPLACEABLE_PLANTS) && !eastUp.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        else if (southBS.getBlock() != Blocks.AIR && southUp.getBlock() != Blocks.AIR) {
            return !southBS.isIn(BlockTags.REPLACEABLE_PLANTS) && !southUp.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        else if (westBS.getBlock() != Blocks.AIR && westUp.getBlock() != Blocks.AIR) {
            return !westBS.isIn(BlockTags.REPLACEABLE_PLANTS) && !westUp.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        return false;
    }

    public static boolean canContinueClimb(PlayerEntity player, BlockPos blockPos) {
        World world = player.getWorld();

        BlockPos north = blockPos.offset(Direction.NORTH, 1);
        BlockPos east = blockPos.offset(Direction.EAST, 1);
        BlockPos south = blockPos.offset(Direction.SOUTH, 1);
        BlockPos west = blockPos.offset(Direction.WEST, 1);

        BlockState northBS = world.getBlockState(north);
        BlockState eastBS = world.getBlockState(east);
        BlockState southBS = world.getBlockState(south);
        BlockState westBS = world.getBlockState(west);

        if (northBS.getBlock() != Blocks.AIR) {
            return !northBS.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        else if (eastBS.getBlock() != Blocks.AIR ) {
            return !eastBS.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        else if (southBS.getBlock() != Blocks.AIR) {
            return !southBS.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        else if (westBS.getBlock() != Blocks.AIR) {
            return !westBS.isIn(BlockTags.REPLACEABLE_PLANTS);
        }
        return false;
    }
}
