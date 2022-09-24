package com.technovision.advancedgenetics.api.block;

import com.technovision.advancedgenetics.api.blockentity.AbstractProcessingBlockEntity;
import com.technovision.advancedgenetics.api.blockentity.ProcessingBlockEntity;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public abstract class AbstractGeneticsBlock extends BlockWithEntity implements BlockEntityProvider {

    private final BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFunction;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    protected AbstractGeneticsBlock(BiFunction<BlockPos, BlockState, BlockEntity> blockEntity) {
        super(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL));
        blockEntityFunction = blockEntity;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ProcessingBlockEntity processingBlockEntity) {
                processingBlockEntity.dropContents();
                world.updateComparators(pos, this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFunction.apply(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return ActionResult.PASS;
        if (world.getBlockEntity(pos) instanceof AbstractProcessingBlockEntity processingBlockEntity) {
            ItemStack stackInHand = player.getStackInHand(hand);
            // Use overclocker on machine
            if (stackInHand.getItem() == ItemRegistry.OVERCLOCKER) {
                if (processingBlockEntity.canOverclock()) {
                    processingBlockEntity.incrementOverclock();
                    if (!player.isCreative()) stackInHand.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
            // Use crowbar on machine to remove overclock
            else if (stackInHand.getItem() == ItemRegistry.CROWBAR) {
                if (processingBlockEntity.getOverclock() > 0) {
                    processingBlockEntity.decrementOverclock();
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.OVERCLOCKER));
                    stackInHand.damage(1, player, (e) -> player.sendToolBreakStatus(player.getActiveHand()));
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }
}
