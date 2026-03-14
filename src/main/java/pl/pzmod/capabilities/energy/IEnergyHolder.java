package pl.pzmod.capabilities.energy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.energy.EnergyHandler;

public interface IEnergyHolder<H> {
    @Nullable
    EnergyHandler getEnergyHandler(@NotNull H holder);

    boolean canHandleEnergy();
}
