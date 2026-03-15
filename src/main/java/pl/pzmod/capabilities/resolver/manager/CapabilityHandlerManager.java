package pl.pzmod.capabilities.resolver.manager;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.holder.IHolder;
import pl.pzmod.capabilities.resolver.SidedCapabilityResolver;

import java.util.List;
import java.util.function.BiFunction;

public class CapabilityHandlerManager<H extends IHolder, C, T, S extends T> extends SidedCapabilityResolver<T, S>
        implements ICapabilityHandlerManager<C, T> {
    private final H holder;
    private final BlockCapability<T, @Nullable Direction> blockCapability;
    private final BiFunction<H, @Nullable Direction, List<C>> containerGetter;

    public CapabilityHandlerManager(H holder,
                                    S baseHandler,
                                    ProxyCreator<T, S> proxyCreator,
                                    BlockCapability<T, @Nullable Direction> blockCapability,
                                    BiFunction<H, @Nullable Direction, List<C>> containerGetter) {
        super(baseHandler, proxyCreator);
        this.holder = holder;
        this.blockCapability = blockCapability;
        this.containerGetter = containerGetter;
    }

    @Override
    public @NotNull BlockCapability<T, @Nullable Direction> supportedCapability() {
        return blockCapability;
    }

    @Override
    public @NotNull List<C> getContainers(@Nullable Direction side) {
        return containerGetter.apply(holder, side);
    }
}
