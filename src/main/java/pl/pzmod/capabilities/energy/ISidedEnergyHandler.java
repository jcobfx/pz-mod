package pl.pzmod.capabilities.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;

public interface ISidedEnergyHandler extends IEnergyHandler {
    int getStorages(@Nullable Direction side);

    void setEnergyInStorage(int storage, int energy, @Nullable Direction side);

    int getEnergyInStorage(int storage, @Nullable Direction side);

    int getStorageMaxEnergy(int storage, @Nullable Direction side);

    /**
     *
     * @param storage index of storage
     * @param energy  amount of energy to insert
     * @param action  action to perform
     * @param side    side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return amount of energy that could not be inserted
     */
    int insertEnergy(int storage, int energy, @NotNull Action action, @Nullable Direction side);

    /**
     *
     * @param storage index of storage
     * @param energy  amount of energy to extract
     * @param action  action to perform
     * @param side    side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return amount of extracted energy
     */
    int extractEnergy(int storage, int energy, @NotNull Action action, @Nullable Direction side);

    default @Nullable Direction getEnergyHandlerSideFor() {
        return null;
    }

    @Override
    default int getStorages() {
        return getStorages(getEnergyHandlerSideFor());
    }

    @Override
    default void setEnergyInStorage(int storage, int energy) {
        setEnergyInStorage(storage, energy, getEnergyHandlerSideFor());
    }

    @Override
    default int insertEnergy(int storage, int energy, @NotNull Action action) {
        return insertEnergy(storage, energy, action, getEnergyHandlerSideFor());
    }

    @Override
    default int extractEnergy(int storage, int energy, @NotNull Action action) {
        return extractEnergy(storage, energy, action, getEnergyHandlerSideFor());
    }

    @Override
    default int getEnergyInStorage(int storage) {
        return getEnergyInStorage(storage, getEnergyHandlerSideFor());
    }

    @Override
    default int getStorageMaxEnergy(int storage) {
        return getStorageMaxEnergy(storage, getEnergyHandlerSideFor());
    }
}
