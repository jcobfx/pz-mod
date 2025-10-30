package pl.pzmod.capabilities.energy;

import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.energy.IEnergyStorage;
import pl.pzmod.capabilities.CapabilityResolver;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class EnergyCapabilityResolver<CONTEXT> extends CapabilityResolver<CONTEXT, AttachedEnergy> implements IEnergyStorage {
    private final int capacity;
    private final int maxTransfer;

    public EnergyCapabilityResolver(MutableDataComponentHolder parent,
                                    CONTEXT context,
                                    int capacity,
                                    int maxTransfer,
                                    Predicate<CONTEXT> canReceive,
                                    Predicate<CONTEXT> canExtract) {
        super(parent, PZDataComponents.ATTACHED_ENERGY, context, canReceive, canExtract);
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (canReceive() && toReceive > 0) {
            int energy = getEnergyStored();
            int energyReceived = Mth.clamp(capacity - energy, 0, Math.min(maxTransfer, toReceive));
            if (!simulate && energyReceived > 0) {
                setEnergy(energy + energyReceived);
            }

            return energyReceived;
        } else {
            return 0;
        }
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (canExtract() && toExtract > 0) {
            int energy = getEnergyStored();
            int energyExtracted = Math.min(energy, Math.min(maxTransfer, toExtract));
            if (!simulate && energyExtracted > 0) {
                setEnergy(energy - energyExtracted);
            }

            return energyExtracted;
        } else {
            return 0;
        }
    }

    protected void setEnergy(int energy) {
        int realEnergy = Mth.clamp(energy, 0, this.capacity);
        setAttached(new AttachedEnergy(realEnergy));
    }

    @Override
    public boolean canExtract() {
        return super.canExtract() && maxTransfer > 0;
    }

    @Override
    public boolean canReceive() {
        return super.canInsert() && maxTransfer > 0;
    }

    @Override
    public int getEnergyStored() {
        int rawEnergy = getAttached(AttachedEnergy.EMPTY).energy();
        return Mth.clamp(rawEnergy, 0, this.capacity);
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }
}
