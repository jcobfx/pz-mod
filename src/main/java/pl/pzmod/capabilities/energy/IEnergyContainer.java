package pl.pzmod.capabilities.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;

public interface IEnergyContainer {
    int getMaxEnergy();

    int getEnergy();

    void setEnergy(int energy);

    default int clampEnergy(int energy) {
        return Math.clamp(energy, 0, getMaxEnergy());
    }

    default boolean isEmpty() {
        return getEnergy() == 0;
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
        int toAdd = Math.min(energy, needed);
        if (action.execute()) {
            setEnergy(getEnergy() + toAdd);
        }
        return energy - toAdd;
    }

    /**
     *
     * @param energy         amount of energy to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return amount of extracted energy
     */
    default int extract(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0 || isEmpty()) {
            return 0;
        }
        int ret = Math.min(getEnergy(), energy);
        if (ret > 0 && action.execute()) {
            setEnergy(getEnergy() - ret);
        }
        return ret;
    }
}
