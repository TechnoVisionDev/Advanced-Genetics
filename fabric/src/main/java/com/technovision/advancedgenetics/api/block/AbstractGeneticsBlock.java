package com.technovision.advancedgenetics.api.block;

import com.technovision.advancedgenetics.api.blockentity.AbstractProcessingBlockEntity;
import com.technovision.advancedgenetics.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import java.util.function.BiFunction;

public abstract class AbstractGeneticsBlock extends BaseEntityBlock {
    private final BiFunction<BlockPos, BlockState, BlockEntity> blockEntityFunction;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected AbstractGeneticsBlock(BlockBehaviour.Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> blockEntity) {
        super(properties);
        blockEntityFunction = blockEntity;
    }

    public abstract int getEnergyRequirement();

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityFunction.apply(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof AbstractProcessingBlockEntity machine) {
            if (stack.getItem() == ItemRegistry.OVERCLOCKER && machine.canOverclock()) {
                machine.incrementOverclock();
                if (!player.isCreative()) stack.shrink(1);
                machine.setChanged();
                return InteractionResult.SUCCESS;
            } else if (stack.getItem() == ItemRegistry.CROWBAR && machine.getOverclock() > 0) {
                machine.decrementOverclock();
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.OVERCLOCKER));
                stack.hurtAndBreak(1, player, hand);
                machine.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        return useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            MenuProvider provider = state.getMenuProvider(level, pos);
            if (provider != null) player.openMenu(provider);
        }
        return InteractionResult.SUCCESS;
    }
}
