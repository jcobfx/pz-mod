package pl.pzmod.data.containers;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.RelativeSide;

import java.util.*;
import java.util.function.Supplier;

public abstract class MutableContainerHandler<T> extends AbstractList<T> implements IMutableContainerHandler<T> {
    private final Map<RelativeSide, List<T>> directionalContainers;
    private final List<T> containers;
    private final @Nullable Supplier<Direction> facing;

    protected MutableContainerHandler(@Nullable Supplier<Direction> facing) {
        this.directionalContainers = new EnumMap<>(RelativeSide.class);
        this.containers = new ArrayList<>();
        this.facing = facing;
    }

    @Override
    public void addContainer(@NotNull T container) {
        containers.add(container);
    }

    @Override
    public void addContainer(@NotNull T container, RelativeSide... sides) {
        containers.add(container);
        for (RelativeSide side : sides) {
            directionalContainers.computeIfAbsent(side, s -> new ArrayList<>()).add(container);
        }
    }

    @Override
    public @NotNull List<T> getContainers(@Nullable Direction side) {
        if (side == null || facing == null || directionalContainers.isEmpty()) {
            return Collections.unmodifiableList(containers);
        }
        var sideContainers = directionalContainers.get(RelativeSide.fromDirections(facing.get(), side));
        if (sideContainers == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(sideContainers);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MutableContainerHandler<?> that = (MutableContainerHandler<?>) o;
        return Objects.equals(directionalContainers, that.directionalContainers)
                && Objects.equals(containers, that.containers) && Objects.equals(facing, that.facing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), directionalContainers, containers, facing);
    }

    @Override
    public int size() {
        return containers.size();
    }

    @Override
    public T get(int index) {
        return containers.get(index);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new ContainerIterator();
    }

    private class ContainerIterator implements Iterator<T> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return containers.get(cursor++);
        }
    }
}
