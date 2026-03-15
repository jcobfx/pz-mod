package pl.pzmod.capabilities.holder.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.capabilities.holder.ContainerHolder;

import java.util.List;
import java.util.function.Supplier;

public class EnergyHolder extends ContainerHolder<IEnergyContainer> implements IMutableEnergyHolder {
    EnergyHolder(Supplier<Direction> facingSupplier) {
        super(facingSupplier);
    }

    @Override
    public void addEnergyContainer(@NotNull IEnergyContainer container, RelativeSide... sides) {
        addContainerInternal(container, sides);
    }

    @Override
    public @NotNull List<IEnergyContainer> getEnergyContainers(@Nullable Direction side) {
        return getContainers(side);
    }
}
