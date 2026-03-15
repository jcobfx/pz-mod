package pl.pzmod.capabilities.holder.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.energy.IEnergyContainer;

public interface IMutableEnergyHolder extends IEnergyHolder {
    void addEnergyContainer(@NotNull IEnergyContainer container, RelativeSide... sides);
}
