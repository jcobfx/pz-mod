package pl.pzmod.capabilities.proxy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.energy.IEnergyHandler;
import pl.pzmod.capabilities.energy.ISidedEnergyHandler;

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
    public void setEnergyInStorage(int storage, int energy) {
        sidedEnergyHandler.setEnergyInStorage(storage, energy, side);
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
    public int getEnergyInStorage(int storage) {
        return sidedEnergyHandler.getEnergyInStorage(storage, side);
    }

    @Override
    public int getStorageMaxEnergy(int storage) {
        return sidedEnergyHandler.getStorageMaxEnergy(storage, side);
    }
}
