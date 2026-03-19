package pl.pzmod.capabilities.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;

public interface IEnergyHandler extends IEnergyStorage {
    int getStorages();

    int getEnergyInStorage(int storage);

    void setEnergyInStorage(int storage, int energy);

    int getStorageMaxEnergy(int storage);

    /**
     *
     * @param storage index of storage
     * @param energy  amount of energy to insert
     * @param action  action to perform
     * @return amount of energy that could not be inserted
     */
    int insertEnergy(int storage, int energy, @NotNull Action action);

    /**
     *
     * @param storage index of storage
     * @param energy  amount of energy to extract
     * @param action  action to perform
     * @return amount of extracted energy
     */
    int extractEnergy(int storage, int energy, @NotNull Action action);

    @Override
    default int getEnergyStored() {
        return getEnergyInStorage(0);
    }

    @Override
    default int getMaxEnergyStored() {
        return getStorageMaxEnergy(0);
    }

    @Override
    default boolean canExtract() {
        return true;
    }

    @Override
    default boolean canReceive() {
        return true;
    }

    /**
     *
     * @param energy   amount of energy to receive
     * @param simulate if true, the insertion is only simulated and no changes should be made to the container
     * @return amount of received energy
     */
    @Override
    default int receiveEnergy(int energy, boolean simulate) {
        return energy - insertEnergy(0, energy, Action.get(!simulate));
    }

    /**
     *
     * @param energy   amount of energy to extract
     * @param simulate if true, the insertion is only simulated and no changes should be made to the container
     * @return amount of extracted energy
     */
    @Override
    default int extractEnergy(int energy, boolean simulate) {
        return extractEnergy(0, energy, Action.get(!simulate));
    }
}
