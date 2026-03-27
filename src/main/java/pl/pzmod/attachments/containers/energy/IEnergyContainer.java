package pl.pzmod.attachments.containers.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;

public interface IEnergyContainer {
    int getMaxEnergy();

    int getEnergy();

    void setEnergy(int energy);

    int getRate();

    boolean canInsert(@NotNull AutomationType automationType);

    boolean canExtract(@NotNull AutomationType automationType);

    default int clampEnergy(int energy) {
        return Math.clamp(energy, 0, getMaxEnergy());
    }

    /**
     *
     * @param energy         amount of energy to insert
     * @param action         action to perform
     * @param automationType automation type
     * @return amount of energy that could not be inserted
     */
    default int insert(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0) {
            return energy;
        }
        int needed = Math.max(0, getMaxEnergy() - getEnergy());
        if (needed == 0) {
            return energy;
        }
        int toInsert = Math.min(energy, needed);
        if (action.execute()) {
            setEnergy(getEnergy() + toInsert);
        }
        return energy - toInsert;
    }

    /**
     *
     * @param energy         amount of energy to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return amount of extracted energy
     */
    default int extract(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0 || getEnergy() == 0) {
            return 0;
        }
        int toExtract = Math.min(getEnergy(), energy);
        if (toExtract > 0 && action.execute()) {
            setEnergy(getEnergy() - toExtract);
        }
        return toExtract;
    }
}
