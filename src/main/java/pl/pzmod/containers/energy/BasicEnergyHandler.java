package pl.pzmod.containers.energy;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class BasicEnergyHandler<HOLDER extends MutableDataComponentHolder> implements ISidedEnergyStorage {
    private final HOLDER holder;
    private final int capacity;
    private final int maxTransfer;
    private final Predicate<@Nullable Direction> canExtract;
    private final Predicate<@Nullable Direction> canInsert;

    public BasicEnergyHandler(HOLDER holder,
                              int capacity,
                              int maxTransfer,
                              Predicate<@Nullable Direction> canExtract,
                              Predicate<@Nullable Direction> canInsert) {
        this.holder = holder;
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        this.canExtract = canExtract;
        this.canInsert = canInsert;
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
    public int receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side) {
        int storedEnergy = getAttached().energy();
        int energyReceived = Math.min(capacity - storedEnergy, Math.min(maxTransfer, maxReceive));
        if (!simulate && energyReceived > 0) {
            setAttachedEnergy(storedEnergy + energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side) {
        int storedEnergy = getAttached().energy();
        int energyExtracted = Math.min(storedEnergy, Math.min(maxTransfer, maxExtract));
        if (!simulate && energyExtracted > 0) {
            setAttachedEnergy(storedEnergy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public boolean canExtract(@Nullable Direction side) {
        return maxTransfer > 0 && canExtract.test(side);
    }

    @Override
    public boolean canReceive(@Nullable Direction side) {
        return maxTransfer > 0 && canInsert.test(side);
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
