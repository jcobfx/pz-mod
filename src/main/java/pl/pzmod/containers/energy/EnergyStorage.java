package pl.pzmod.containers.energy;

import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.energy.IEnergyStorage;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZDataComponents;

import java.util.List;

public class EnergyStorage implements IEnergyStorage {
    private final MutableDataComponentHolder holder;
    private final int capacity;
    private final int maxTransfer;

    public EnergyStorage(MutableDataComponentHolder holder,
                         int capacity,
                         int maxTransfer) {
        this.holder = holder;
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
    }

    private int getEnergy() {
        return holder.getOrDefault(PZDataComponents.ATTACHED_ENERGY, AttachedEnergy.create(1)).containers()
                .getFirst();
    }

    private void setEnergy(int energy) {
        holder.set(PZDataComponents.ATTACHED_ENERGY, new AttachedEnergy(List.of(energy)));
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate) {
        int storedEnergy = getEnergy();
        int energyReceived = Math.min(capacity - storedEnergy, Math.min(maxTransfer, energy));
        if (!simulate && energyReceived > 0) {
            setEnergy(storedEnergy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate) {
        int storedEnergy = getEnergy();
        int energyExtracted = Math.min(storedEnergy, Math.min(maxTransfer, energy));
        if (!simulate && energyExtracted > 0) {
            setEnergy(storedEnergy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return getEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxTransfer > 0;
    }

    @Override
    public boolean canReceive() {
        return maxTransfer > 0;
    }
}
