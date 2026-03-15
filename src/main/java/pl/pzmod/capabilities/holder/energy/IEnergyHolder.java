package pl.pzmod.capabilities.holder.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.capabilities.holder.IHolder;

import java.util.List;

public interface IEnergyHolder extends IHolder {
    @NotNull
    List<IEnergyContainer> getEnergyContainers(@Nullable Direction side);
}
