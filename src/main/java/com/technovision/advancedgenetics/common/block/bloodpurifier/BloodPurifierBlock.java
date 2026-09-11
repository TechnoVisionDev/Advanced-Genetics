package com.technovision.advancedgenetics.common.block.bloodpurifier;

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

public class BloodPurifierBlock extends AbstractGeneticsBlock {
    public static final MapCodec<BloodPurifierBlock> CODEC = simpleCodec(BloodPurifierBlock::new);

    public BloodPurifierBlock(BlockBehaviour.Properties properties) {
        super(properties, BloodPurifierBlockEntity::new);
    }

    @Override
    protected MapCodec<BloodPurifierBlock> codec() {
        return CODEC;
    }

    @Override
    public int getEnergyRequirement() {
        return Config.Common.bloodPurifierEnergyPerTick.get();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof BloodPurifierBlockEntity machine) machine.tick();
        };
    }
}
