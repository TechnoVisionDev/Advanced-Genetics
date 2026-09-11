package com.technovision.advancedgenetics.common.block.plasmidinfuser;

import com.mojang.serialization.MapCodec;
import com.technovision.advancedgenetics.Config;
import com.technovision.advancedgenetics.api.block.AbstractGeneticsBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlasmidInfuserBlock extends AbstractGeneticsBlock {
    public static final MapCodec<PlasmidInfuserBlock> CODEC = simpleCodec(PlasmidInfuserBlock::new);

    public PlasmidInfuserBlock(BlockBehaviour.Properties properties) {
        super(properties, PlasmidInfuserBlockEntity::new);
    }

    @Override
    protected MapCodec<PlasmidInfuserBlock> codec() {
        return CODEC;
    }

    @Override
    public int getEnergyRequirement() {
        return Config.Common.plasmidInfuserEnergyPerTick.get();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof PlasmidInfuserBlockEntity machine) machine.tick();
        };
    }
}
