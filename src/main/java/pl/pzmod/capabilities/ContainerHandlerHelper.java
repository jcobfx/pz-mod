package pl.pzmod.capabilities;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.IMutableContainerHandler;

public class ContainerHandlerHelper<T, H extends IMutableContainerHandler<T>> {
    public static <T, H extends IMutableContainerHandler<T>> ContainerHandlerHelper<T, H> builder(@NotNull H handler,
                                                                                                  @NotNull IContainerHolder holder) {
        return new ContainerHandlerHelper<>(handler, holder);
    }

    private final H handler;
    private final IContainerHolder holder;

    private int current;
    private boolean built;

    private ContainerHandlerHelper(H handler, IContainerHolder holder) {
        this.handler = handler;
        this.holder = holder;
        this.current = 0;
        this.built = false;
    }

    public @NotNull ContainerHandlerHelper<T, H> addContainer(@NotNull IContainerConfig<T> container) {
        checkBuilt();
        handler.addContainer(container.createContainer(holder, current++));
        return this;
    }

    public @NotNull ContainerHandlerHelper<T, H> addContainer(@NotNull IContainerConfig<T> container, RelativeSide... sides) {
        checkBuilt();
        handler.addContainer(container.createContainer(holder, current++), sides);
        return this;
    }

    public H build() {
        checkBuilt();
        if (current != holder.getContainerCount()) {
            throw new IllegalStateException("Invalid amount of created containers.");
        }
        built = true;
        return handler;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Config already built.");
        }
    }
}
