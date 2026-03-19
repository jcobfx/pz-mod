package pl.pzmod.capabilities.holder;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.RelativeSide;

import java.util.*;
import java.util.function.Supplier;

public abstract class ContainerHolder<C> {
    private final Map<RelativeSide, List<C>> directionalContainers;
    private final List<C> containers;
    private final Supplier<Direction> facingSupplier;

    protected ContainerHolder(Supplier<Direction> facingSupplier) {
        this.directionalContainers = new EnumMap<>(RelativeSide.class);
        this.containers = new ArrayList<>();
        this.facingSupplier = facingSupplier;
    }

    protected void addContainerInternal(@NotNull C container, RelativeSide... sides) {
        containers.add(container);
        for (RelativeSide side : sides) {
            directionalContainers.computeIfAbsent(side, s -> new ArrayList<>()).add(container);
        }
    }

    public @NotNull List<C> getContainers(@Nullable Direction side) {
        if (side == null || directionalContainers.isEmpty()) {
            return containers;
        }
        return directionalContainers.getOrDefault(RelativeSide.fromDirections(facingSupplier.get(), side), Collections.emptyList());
    }
}
