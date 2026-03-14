package pl.pzmod.data.containers.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.MutableContainerHandler;

import java.util.function.Supplier;

public class EnergyHandler extends MutableContainerHandler<IEnergyContainer> implements ISidedEnergyHandler {
    public EnergyHandler() {
        this(null);
    }

    public EnergyHandler(@Nullable Supplier<Direction> facing) {
        super(facing);
    }

    @Override
    public int getStorages(@Nullable Direction side) {
        return getContainers(side).size();
    }

    @Override
    public int insertEnergy(int storage, int energy, @Nullable Direction side, @NotNull Action action) {
        return getContainers(side).get(storage).insert(energy, action, AutomationType.handler(side));
    }

    @Override
    public int extractEnergy(int storage, int energy, @Nullable Direction side, @NotNull Action action) {
        return getContainers(side).get(storage).extract(energy, action, AutomationType.handler(side));
    }

    @Override
    public int getEnergy(int storage, @Nullable Direction side) {
        return getContainers(side).get(storage).getEnergy();
    }

    @Override
    public int getEnergyCapacity(int storage, @Nullable Direction side) {
        return getContainers(side).get(storage).getCapacity();
    }

    @Override
    public boolean canInsert(int storage, @Nullable Direction side) {
        return getContainers(side).get(storage).canInsert(AutomationType.handler(side));
    }

    @Override
    public boolean canExtract(int storage, @Nullable Direction side) {
        return getContainers(side).get(storage).canExtract(AutomationType.handler(side));
    }
}
