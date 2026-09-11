package com.technovision.advancedgenetics.api.blockentity;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.Config;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Objects;

public abstract class AbstractProcessingBlockEntity extends BlockEntity implements ProcessingBlockEntity, ExtendedMenuProvider<BlockPos>, Nameable {

    private final Component name;
    private int progress = 0;
    private int overclock = 0;
    private int maxOverclock;
    private final SimpleEnergyStorage energyStorage;
    private int maxProgress;
    private final ContainerData propertyDelegate;

    public AbstractProcessingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, long energyCapacity, int maxProgress, int maxOverclock) {
        super(type, pos, state);
        String blockEntityName = Objects.requireNonNull(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(getType())).getPath();
        this.name = Component.translatable(String.format("%s.container.%s", AdvancedGenetics.MOD_ID, blockEntityName));
        energyStorage = new SimpleEnergyStorage(energyCapacity, energyCapacity, energyCapacity) {
            @Override
            protected void onFinalCommit() {
                setChanged();
            }
        };
        this.maxOverclock = maxOverclock;
        this.maxProgress = maxProgress;
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> getProgress();
                    case 1 -> getMaxProgress();
                    case 2 -> (int) getEnergyStorage().getAmount();
                    case 3 -> (int) getEnergyStorage().getCapacity();
                    case 4 -> getOverclock();
                    default -> 0;
                };
            }
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> setProgress(value);
                    case 2 -> insertEnergy(value);
                    case 4 -> setOverclock(value);
                }
            }
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Component getName() {
        return name != null ? name : this.getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    protected Component getDefaultName() {
        return name;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide()) {
            updateRecipe();
            if (canProcessRecipe()) {
                processRecipe();
            } else if (progress > 0) {
                setProgress(0);
                setRecipe(null);
            }
        }
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void incrementProgress() {
        this.progress++;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(ValueOutput nbt) {
        nbt.putInt("progress", progress);
        nbt.putLong("energy", energyStorage.amount);
        nbt.putInt("overclock", overclock);
        super.saveAdditional(nbt);
    }

    @Override
    protected void loadAdditional(ValueInput nbt) {
        super.loadAdditional(nbt);
        setProgress(nbt.getIntOr("progress", 0));
        energyStorage.amount = Math.max(0, Math.min(energyStorage.capacity, nbt.getLongOr("energy", 0)));
        int oc = nbt.getIntOr("overclock", 0);
        if (oc > maxOverclock) oc = maxOverclock;
        setOverclock(oc);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return worldPosition;
    }

    public SimpleEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void insertEnergy(long value) {
        try (Transaction transaction = Transaction.openOuter()) {
            long amountExtracted = getEnergyStorage().insert(value, transaction);
            if (amountExtracted == value) {
                transaction.commit();
            }
        }
    }

    public void extractEnergy(long value) {
        try (Transaction transaction = Transaction.openOuter()) {
            long amountExtracted = getEnergyStorage().extract(value, transaction);
            if (amountExtracted == value) {
                transaction.commit();
            }
        }
    }

    public void forceSync() {
        this.setChanged();
        if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public boolean canOverclock() {
        return overclock < maxOverclock && getMaxProgress() >= 20;
    }

    public void setOverclock(int overclock) {
        this.overclock = overclock;
    }

    public void incrementOverclock() {
        this.overclock++;
    }

    public void decrementOverclock() {
        this.overclock--;
    }

    public int getOverclock() {
        return overclock;
    }

    public int getMaxProgress() {
        int base = 20 * Config.Common.overclockSpeed.get();
        int realMaxProgress = (maxProgress - (base * getOverclock()));
        if (realMaxProgress < 20) realMaxProgress = 20;
        return realMaxProgress;
    }

    public ContainerData getPropertyDelegate() {
        return propertyDelegate;
    }
}
