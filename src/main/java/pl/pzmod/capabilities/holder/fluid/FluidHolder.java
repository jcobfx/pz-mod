package pl.pzmod.capabilities.holder.fluid;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.capabilities.holder.ContainerHolder;

import java.util.List;
import java.util.function.Supplier;

public class FluidHolder extends ContainerHolder<IFluidContainer> implements IFluidHolder {
    public FluidHolder(Supplier<Direction> facingSupplier) {
        super(facingSupplier);
    }

    void addFluidContainer(@NotNull IFluidContainer container, RelativeSide... sides) {
        addContainerInternal(container, sides);
    }

    @Override
    public @NotNull List<IFluidContainer> getFluidContainers(@Nullable Direction side) {
        return getContainers(side);
    }
}
