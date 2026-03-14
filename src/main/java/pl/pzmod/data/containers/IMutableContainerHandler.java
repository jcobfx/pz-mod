package pl.pzmod.data.containers;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;

public interface IMutableContainerHandler<T> extends IContainerHandler<T> {
    void addContainer(@NotNull T container);

    void addContainer(@NotNull T container, RelativeSide... sides);
}
