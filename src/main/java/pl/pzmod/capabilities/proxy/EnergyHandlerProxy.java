package pl.pzmod.capabilities.proxy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.energy.IEnergyHandler;
import pl.pzmod.data.containers.energy.ISidedEnergyHandler;

public class EnergyHandlerProxy implements IEnergyHandler {
    private final ISidedEnergyHandler sidedEnergyHandler;
    private final @Nullable Direction side;

    public EnergyHandlerProxy(ISidedEnergyHandler sidedEnergyHandler, @Nullable Direction side) {
        this.sidedEnergyHandler = sidedEnergyHandler;
        this.side = side;
    }

    @Override
    public int getStorages() {
        return sidedEnergyHandler.getStorages(side);
    }

    @Override
    public void setEnergy(int storage, int energy) {
        sidedEnergyHandler.setEnergy(storage, energy, side);
    }

    @Override
    public int insertEnergy(int storage, int energy, @NotNull Action action) {
        return sidedEnergyHandler.insertEnergy(storage, energy, action, side);
    }

    @Override
    public int extractEnergy(int storage, int energy, @NotNull Action action) {
        return sidedEnergyHandler.extractEnergy(storage, energy, action, side);
    }

    @Override
    public int getEnergy(int storage) {
        return sidedEnergyHandler.getEnergy(storage, side);
    }

    @Override
    public int getEnergyCapacity(int storage) {
        return sidedEnergyHandler.getEnergyCapacity(storage, side);
    }

    @Override
    public boolean canInsert(int storage) {
        return sidedEnergyHandler.canInsert(storage, side);
    }

    @Override
    public boolean canExtract(int storage) {
        return sidedEnergyHandler.canExtract(storage, side);
    }
}
