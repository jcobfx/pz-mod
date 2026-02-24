package pl.pzmod.capabilities.energy;

import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.IEnergyStorage;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;

import javax.annotation.Nullable;

public class BlockEnergyCapabilityResolver implements IEnergyStorage {
    private final GeneratorBlockEntity be;
    private final @Nullable Object context;

    public BlockEnergyCapabilityResolver(GeneratorBlockEntity be, @Nullable Object context) {
        this.be = be;
        this.context = context;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (!canReceive() || toReceive <= 0) return 0;

        int energy = be.getEnergyStored();
        int capacity = be.getCapacity();
        int maxTransfer = be.getMaxTransfer();

        int received = Math.min(capacity - energy, Math.min(maxTransfer, toReceive));

        if (!simulate && received > 0) {
            be.setEnergyStored(energy + received);
            be.setChanged();
        }
        return received;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (!canExtract() || toExtract <= 0) return 0;

        int energy = be.getEnergyStored();
        int maxTransfer = be.getMaxTransfer();

        int extracted = Math.min(energy, Math.min(maxTransfer, toExtract));

        if (!simulate && extracted > 0) {
            be.consumeEnergy(extracted);
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return be.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return be.getCapacity();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}