package pl.pzmod.capabilities.resolver;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class SidedCapabilityResolver<T, S extends T> implements ICapabilityResolver<T, @Nullable Direction> {
    private final ProxyCreator<T, S> proxyCreator;
    private final Map<Direction, T> handlers;
    private final S baseHandler;

    private @Nullable T internalHandler;

    public SidedCapabilityResolver(S baseHandler, ProxyCreator<T, S> proxyCreator) {
        this.proxyCreator = proxyCreator;
        this.handlers = new EnumMap<>(Direction.class);
        this.baseHandler = baseHandler;
    }

    public S getBaseHandler() {
        return baseHandler;
    }

    @Override
    public @Nullable T resolve(@Nullable Direction side) {
        if (side == null) {
            if (internalHandler == null) {
                internalHandler = proxyCreator.create(baseHandler, null);
            }
            return internalHandler;
        }
        return handlers.computeIfAbsent(side, s -> proxyCreator.create(baseHandler, s));
    }

    @Override
    public void invalidate(@Nullable Direction side) {
        if (side == null) {
            internalHandler = null;
        } else {
            handlers.remove(side);
        }
    }

    @Override
    public void invalidateAll() {
        internalHandler = null;
        handlers.clear();
    }

    @FunctionalInterface
    public interface ProxyCreator<T, S extends T> {
        T create(S baseHandler, @Nullable Direction side);
    }
}
