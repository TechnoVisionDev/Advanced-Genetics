package com.technovision.advancedgenetics.common.block.plasmidinjector;

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

public class PlasmidInjectorBlock extends AbstractGeneticsBlock {
    public static final MapCodec<PlasmidInjectorBlock> CODEC = simpleCodec(PlasmidInjectorBlock::new);

    public PlasmidInjectorBlock(BlockBehaviour.Properties properties) {
        super(properties, PlasmidInjectorBlockEntity::new);
    }

    @Override
    protected MapCodec<PlasmidInjectorBlock> codec() {
        return CODEC;
    }

    @Override
    public int getEnergyRequirement() {
        return Config.Common.plasmidInjectorEnergyPerTick.get();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof PlasmidInjectorBlockEntity machine) machine.tick();
        };
    }
}
