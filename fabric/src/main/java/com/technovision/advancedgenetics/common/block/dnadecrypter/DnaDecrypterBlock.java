package com.technovision.advancedgenetics.common.block.dnadecrypter;

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

public class DnaDecrypterBlock extends AbstractGeneticsBlock {
    public static final MapCodec<DnaDecrypterBlock> CODEC = simpleCodec(DnaDecrypterBlock::new);

    public DnaDecrypterBlock(BlockBehaviour.Properties properties) {
        super(properties, DnaDecrypterBlockEntity::new);
    }

    @Override
    protected MapCodec<DnaDecrypterBlock> codec() {
        return CODEC;
    }

    @Override
    public int getEnergyRequirement() {
        return Config.Common.dnaDecrypterEnergyPerTick.get();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof DnaDecrypterBlockEntity machine) machine.tick();
        };
    }
}
