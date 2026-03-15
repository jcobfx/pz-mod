package pl.pzmod.capabilities.resolver;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Supplier;

public class CapabilityResolver<T, C> implements ICapabilityResolver<T, C> {
    private final Supplier<T> supplier;

    private @Nullable T cachedHandler;

    public CapabilityResolver(Supplier<T> handlerSupplier) {
        this.supplier = handlerSupplier;
    }

    @Override
    public @Nullable T resolve(@UnknownNullability C context) {
        if (cachedHandler == null) {
            cachedHandler = supplier.get();
        }
        return cachedHandler;
    }

    @Override
    public void invalidate(@UnknownNullability C context) {
        cachedHandler = null;
    }

    @Override
    public void invalidateAll() {
        cachedHandler = null;
    }
}
