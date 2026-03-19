package pl.pzmod.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.PZMod;
import pl.pzmod.capabilities.resolver.ICapabilityResolver;
import pl.pzmod.capabilities.resolver.manager.ICapabilityHandlerManager;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CapabilityCache {
    private final Map<BlockCapability<?, @Nullable Direction>, ICapabilityResolver<?, @Nullable Direction>> resolvers;

    public CapabilityCache() {
        resolvers = new IdentityHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable ICapabilityResolver<T, @Nullable Direction> getCapabilityResolver(BlockCapability<T, @Nullable Direction> capability) {
        return (ICapabilityResolver<T, Direction>) resolvers.get(capability);
    }

    public <T> void addCapabilityManager(ICapabilityHandlerManager<?, T> capabilityHandlerManager) {
        addCapabilityResolver(capabilityHandlerManager.supportedCapability(), capabilityHandlerManager);
    }

    public <T> void addCapabilityResolver(BlockCapability<T, @Nullable Direction> capability, ICapabilityResolver<T, @Nullable Direction> resolver) {
        if (resolvers.put(capability, resolver) != null) {
            PZMod.LOGGER.warn("Multiple capability resolvers registered for {}. Overriding", capability.name(), new Exception());
        }
    }

    public <T> @NotNull ICapabilityResolver<T, @Nullable Direction> getCapabilityResolver(BlockCapability<T, @Nullable Direction> capability,
                                                                                          Supplier<ICapabilityResolver<T, @Nullable Direction>> resolver) {
        var knownResolver = getCapabilityResolver(capability);
        if (knownResolver == null) {
            knownResolver = resolver.get();
            addCapabilityResolver(capability, knownResolver);
        }
        return knownResolver;
    }

    public void invalidate(BlockCapability<?, @Nullable Direction> capability, @Nullable Direction side) {
        var capabilityResolver = resolvers.get(capability);
        if (capabilityResolver != null) {
            capabilityResolver.invalidate(side);
        }
    }

    public void invalidateAll(BlockCapability<?, @Nullable Direction> capability) {
        var capabilityResolver = resolvers.get(capability);
        if (capabilityResolver != null) {
            capabilityResolver.invalidateAll();
        }
    }

    public void invalidateAll() {
        resolvers.values().forEach(ICapabilityResolver::invalidateAll);
    }
}
