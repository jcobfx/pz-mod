package pl.pzmod.capabilities.energy;

import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.energy.IEnergyStorage;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class EnergyCapabilityResolver<HOLDER extends MutableDataComponentHolder, CONTEXT> implements IEnergyStorage {
    private final HOLDER holder;
    private final int capacity;
    private final int maxTransfer;

    private final CONTEXT context;
    private final Predicate<CONTEXT> canExtract;
    private final Predicate<CONTEXT> canReceive;

    public EnergyCapabilityResolver(HOLDER holder,
                                    int capacity,
                                    int maxTransfer,
                                    CONTEXT context,
                                    Predicate<CONTEXT> canExtract,
                                    Predicate<CONTEXT> canReceive) {
        this.holder = holder;
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        this.context = context;
        this.canExtract = canExtract;
        this.canReceive = canReceive;
    }

    private AttachedEnergy getAttached() {
        return holder.getOrDefault(PZDataComponents.ATTACHED_ENERGY, AttachedEnergy.empty());
    }

    private void setAttached(AttachedEnergy attachedEnergy) {
        holder.set(PZDataComponents.ATTACHED_ENERGY, attachedEnergy);
    }

    private void setAttachedEnergy(int energy) {
        setAttached(new AttachedEnergy(energy));
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int storedEnergy = getAttached().energy();
        int energyReceived = Math.min(capacity - storedEnergy, Math.min(maxTransfer, maxReceive));
        if (!simulate && energyReceived > 0) {
            setAttachedEnergy(storedEnergy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int storedEnergy = getAttached().energy();
        int energyExtracted = Math.min(storedEnergy, Math.min(maxTransfer, maxExtract));
        if (!simulate && energyExtracted > 0) {
            setAttachedEnergy(storedEnergy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public boolean canExtract() {
        return maxTransfer > 0 && canExtract.test(context);
    }

    @Override
    public boolean canReceive() {
        return maxTransfer > 0 && canReceive.test(context);
    }

    @Override
    public int getEnergyStored() {
        return getAttached().energy();
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }
}
