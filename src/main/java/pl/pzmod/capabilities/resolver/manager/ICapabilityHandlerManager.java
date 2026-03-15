package pl.pzmod.capabilities.resolver.manager;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.resolver.ICapabilityResolver;

import java.util.List;

public interface ICapabilityHandlerManager<C, T> extends ICapabilityResolver<T, @Nullable Direction> {
    @NotNull
    BlockCapability<T, @Nullable Direction> supportedCapability();

    @NotNull
    List<C> getContainers(@Nullable Direction side);
}
