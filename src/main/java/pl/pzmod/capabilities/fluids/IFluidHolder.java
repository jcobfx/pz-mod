package pl.pzmod.capabilities.fluids;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.fluids.FluidHandler;

public interface IFluidHolder<H> {
    @Nullable
    FluidHandler getFluidHandler(@NotNull H holder);

    boolean canHandleFluids();
}
