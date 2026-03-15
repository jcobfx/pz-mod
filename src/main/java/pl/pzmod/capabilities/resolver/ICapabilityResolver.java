package pl.pzmod.capabilities.resolver;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public interface ICapabilityResolver<T, C> {
    @Nullable
    T resolve(@UnknownNullability C context);

    void invalidate(@UnknownNullability C context);

    void invalidateAll();
}
