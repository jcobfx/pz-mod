package pl.pzmod.capabilities.holder;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.RelativeSide;

import java.util.*;
import java.util.function.Supplier;

public abstract class ContainerHolder<T> implements IHolder {
    private final Map<RelativeSide, List<T>> directionalContainers;
    private final List<T> inventoryContainers;
    private final Supplier<Direction> facingSupplier;

    protected ContainerHolder(Supplier<Direction> facingSupplier) {
        this.directionalContainers = new EnumMap<>(RelativeSide.class);
        this.inventoryContainers = new ArrayList<>();
        this.facingSupplier = facingSupplier;
    }

    protected void addContainerInternal(@NotNull T container, RelativeSide... sides) {
        inventoryContainers.add(container);
        for (RelativeSide side : sides) {
            directionalContainers.computeIfAbsent(side, k -> new ArrayList<>()).add(container);
        }
    }

    @NotNull
    public List<T> getContainers(@Nullable Direction side) {
        if (side == null || directionalContainers.isEmpty()) {
            return inventoryContainers;
        }
        List<T> slots = directionalContainers.get(RelativeSide.fromDirections(facingSupplier.get(), side));
        if (slots == null) {
            return Collections.emptyList();
        }
        return slots;
    }
}
