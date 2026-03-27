package pl.pzmod.capabilities.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.energy.IEnergyContainer;

import java.util.List;
import java.util.Optional;

public interface IPZEnergyHandler extends ISidedEnergyHandler {
    @NotNull
    List<IEnergyContainer> getEnergyContainers(@Nullable Direction side);

    default Optional<IEnergyContainer> getEnergyContainer(int storage, @Nullable Direction side) {
        var energyContainers = getEnergyContainers(side);
        return Optional.ofNullable(storage >= 0 && storage < energyContainers.size() ? energyContainers.get(storage) : null);
    }

    default boolean hasEnergyContainers() {
        return true;
    }

    @Override
    default int getStorages(@Nullable Direction side) {
        return getEnergyContainers(side).size();
    }

    @Override
    default void setEnergyInStorage(int storage, int energy, @Nullable Direction side) {
        getEnergyContainer(storage, side).ifPresent(energyContainer -> energyContainer.setEnergy(energy));
    }

    @Override
    default int insertEnergy(int storage, int energy, @NotNull Action action, @Nullable Direction side) {
        return getEnergyContainer(storage, side).map(c -> c.insert(energy, action, AutomationType.handler(side))).orElse(energy);
    }

    @Override
    default int extractEnergy(int storage, int energy, @NotNull Action action, @Nullable Direction side) {
        return getEnergyContainer(storage, side).map(c -> c.extract(energy, action, AutomationType.handler(side))).orElse(0);
    }

    @Override
    default int getEnergyInStorage(int storage, @Nullable Direction side) {
        return getEnergyContainer(storage, side).map(IEnergyContainer::getEnergy).orElse(0);
    }

    @Override
    default int getStorageMaxEnergy(int storage, @Nullable Direction side) {
        return getEnergyContainer(storage, side).map(IEnergyContainer::getMaxEnergy).orElse(0);
    }
}
