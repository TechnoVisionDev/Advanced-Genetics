package com.technovision.advancedgenetics.common.block.cellanalyzer;

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

public class CellAnalyzerBlock extends AbstractGeneticsBlock {
    public static final MapCodec<CellAnalyzerBlock> CODEC = simpleCodec(CellAnalyzerBlock::new);

    public CellAnalyzerBlock(BlockBehaviour.Properties properties) {
        super(properties, CellAnalyzerBlockEntity::new);
    }

    @Override
    protected MapCodec<CellAnalyzerBlock> codec() {
        return CODEC;
    }

    @Override
    public int getEnergyRequirement() {
        return Config.Common.cellAnalyzerEnergyPerTick.get();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof CellAnalyzerBlockEntity machine) machine.tick();
        };
    }
}
