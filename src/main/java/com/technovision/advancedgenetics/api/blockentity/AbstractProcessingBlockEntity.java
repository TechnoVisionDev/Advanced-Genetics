package com.technovision.advancedgenetics.api.blockentity;

import com.technovision.advancedgenetics.AdvancedGenetics;
import com.technovision.advancedgenetics.Config;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Objects;

public abstract class AbstractProcessingBlockEntity extends BlockEntity implements ProcessingBlockEntity, ExtendedScreenHandlerFactory, Nameable {

    private final Text name;
    private int progress = 0;
    private int overclock = 0;
    private int maxOverclock;
    private final SimpleEnergyStorage energyStorage;
    private int maxProgress;
    private final PropertyDelegate propertyDelegate;

    public AbstractProcessingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, long energyCapacity, int maxProgress, int maxOverclock) {
        super(type, pos, state);
        String blockEntityName = Objects.requireNonNull(Registry.BLOCK_ENTITY_TYPE.getId(getType())).getPath();
        this.name = Text.translatable(String.format("%s.container.%s", AdvancedGenetics.MOD_ID, blockEntityName));
        energyStorage = new SimpleEnergyStorage(energyCapacity, energyCapacity, energyCapacity) {
            @Override
            protected void onFinalCommit() {
                markDirty();
            }
        };
        this.maxOverclock = maxOverclock;
        this.maxProgress = maxProgress;
        this.propertyDelegate = new PropertyDelegate() {
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
            public int size() {
                return 5;
            }
        };
    }

    @Override
    public Text getName() {
        return name != null ? name : this.getDefaultName();
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }

    protected Text getDefaultName() {
        return name;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = super.toInitialChunkDataNbt();
        writeNbt(tag);
        return tag;
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient()) {
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
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("progress", progress);
        nbt.putLong("energy", energyStorage.amount);
        nbt.putInt("overclock", overclock);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setProgress(nbt.getInt("progress"));
        insertEnergy(nbt.getLong("energy"));
        int oc = nbt.getInt("overclock");
        if (oc > maxOverclock) oc = maxOverclock;
        setOverclock(oc);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
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
        this.markDirty();
        world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 3);
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

    public int getOverclock() {
        return overclock;
    }

    public int getMaxProgress() {
        int base = 20 * Config.Common.overclockSpeed.get();
        int realMaxProgress = (maxProgress - (base * getOverclock()));
        if (realMaxProgress < 20) realMaxProgress = 20;
        return realMaxProgress;
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
}
