package pl.pzmod.capabilities.holder.fluid;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.fluid.IFluidContainer;

public interface IMutableFluidHolder extends IFluidHolder {
    void addFluidContainer(@NotNull IFluidContainer container, RelativeSide... sides);
}
