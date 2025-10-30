package pl.pzmod.containers.energy;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public interface ISidedEnergyStorage extends IEnergyStorage {
    int receiveEnergy(int maxReceive, boolean simulate, @Nullable Direction side);

    int extractEnergy(int maxExtract, boolean simulate, @Nullable Direction side);

    boolean canReceive(@Nullable Direction side);

    boolean canExtract(@Nullable Direction side);

    @Override
    default int receiveEnergy(int maxReceive, boolean simulate) {
        return receiveEnergy(maxReceive, simulate, null);
    }

    @Override
    default int extractEnergy(int maxExtract, boolean simulate) {
        return extractEnergy(maxExtract, simulate, null);
    }

    @Override
    default boolean canReceive() {
        return canReceive(null);
    }

    @Override
    default boolean canExtract() {
        return canExtract(null);
    }
}
