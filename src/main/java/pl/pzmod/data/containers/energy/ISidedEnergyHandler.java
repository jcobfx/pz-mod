package pl.pzmod.data.containers.energy;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;

public interface ISidedEnergyHandler extends IEnergyStorage {
    int getStorages(@Nullable Direction side);

    /**
     *
     * @param storage index of storage
     * @param energy  amount of energy to insert
     * @param side    side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @param action  action to perform
     * @return amount of energy that could not be inserted
     */
    int insertEnergy(int storage, int energy, @Nullable Direction side, @NotNull Action action);

    /**
     *
     * @param storage index of storage
     * @param energy  amount of energy to extract
     * @param side    side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @param action  action to perform
     * @return amount of extracted energy
     */
    int extractEnergy(int storage, int energy, @Nullable Direction side, @NotNull Action action);

    int getEnergy(int storage, @Nullable Direction side);

    int getEnergyCapacity(int storage, @Nullable Direction side);

    boolean canInsert(int storage, @Nullable Direction side);

    boolean canExtract(int storage, @Nullable Direction side);

    default @Nullable Direction getDefaultSide() {
        return null;
    }

    /**
     *
     * @param energy   amount of energy to receive
     * @param simulate if true, the insertion is only simulated and no changes should be made to the container
     * @return amount of received energy
     */
    @Override
    default int receiveEnergy(int energy, boolean simulate) {
        return energy - insertEnergy(0, energy, getDefaultSide(), Action.get(!simulate));
    }

    /**
     *
     * @param energy   amount of energy to extract
     * @param simulate if true, the insertion is only simulated and no changes should be made to the container
     * @return amount of extracted energy
     */
    @Override
    default int extractEnergy(int energy, boolean simulate) {
        return extractEnergy(0, energy, getDefaultSide(), Action.get(!simulate));
    }

    @Override
    default int getEnergyStored() {
        return getEnergy(0, null);
    }

    @Override
    default int getMaxEnergyStored() {
        return getEnergyCapacity(0, null);
    }

    @Override
    default boolean canExtract() {
        return canExtract(0, null);
    }

    @Override
    default boolean canReceive() {
        return canInsert(0, null);
    }
}
