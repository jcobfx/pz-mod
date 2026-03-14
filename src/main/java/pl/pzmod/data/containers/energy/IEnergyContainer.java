package pl.pzmod.data.containers.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;

public interface IEnergyContainer {
    int getRate();

    int getCapacity();

    int getEnergy();

    void setEnergy(int energy);

    boolean canInsert(@NotNull AutomationType automationType);

    boolean canExtract(@NotNull AutomationType automationType);

    default int clampEnergy(int energy) {
        return Math.clamp(energy, 0, getCapacity());
    }

    /**
     *
     * @param energy         amount of energy to insert
     * @param action         action to perform
     * @param automationType automation type
     * @return amount of energy that could not be inserted
     */
    default int insert(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0 || !canInsert(automationType)) {
            return 0;
        }
        int stored = getEnergy();
        int received = Math.min(Math.min(getRate(), energy), getCapacity() - stored);
        if (received > 0 && action.execute()) {
            setEnergy(stored + received);
        }
        return energy - received;
    }

    /**
     *
     * @param energy         amount of energy to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return amount of extracted energy
     */
    default int extract(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        int stored = getEnergy();
        if (energy <= 0 || stored == 0 || !canExtract(automationType)) {
            return 0;
        }
        int extracted = Math.min(Math.min(getRate(), energy), stored);
        if (extracted > 0 && action.execute()) {
            setEnergy(stored - extracted);
        }
        return extracted;
    }
}
