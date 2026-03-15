package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.CapabilityCache;
import pl.pzmod.capabilities.resolver.ICapabilityResolver;
import pl.pzmod.capabilities.resolver.manager.ICapabilityHandlerManager;

import java.util.Collection;
import java.util.List;

public abstract class CapabilityBlockEntity extends BlockEntity {
    public static final ICapabilityProvider<CapabilityBlockEntity, @Nullable Direction, IEnergyStorage> ENERGY_HANDLER_PROVIDER;
    public static final ICapabilityProvider<CapabilityBlockEntity, @Nullable Direction, IFluidHandler> FLUID_HANDLER_PROVIDER;
    public static final ICapabilityProvider<CapabilityBlockEntity, @Nullable Direction, IItemHandler> ITEM_HANDLER_PROVIDER;

    static {
        ENERGY_HANDLER_PROVIDER = simpleCapabilityProvider(Capabilities.ENERGY.block());
        FLUID_HANDLER_PROVIDER = simpleCapabilityProvider(Capabilities.FLUID.block());
        ITEM_HANDLER_PROVIDER = simpleCapabilityProvider(Capabilities.ITEM.block());
    }

    private static <T> ICapabilityProvider<CapabilityBlockEntity, @Nullable Direction, T> simpleCapabilityProvider(BlockCapability<T, @Nullable Direction> capability) {
        return (blockEntity, direction) -> {
            var resolver = blockEntity.capabilityCache.getCapabilityResolver(capability);
            return resolver == null ? null : resolver.resolve(direction);
        };
    }

    private final CapabilityCache capabilityCache;

    protected CapabilityBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        capabilityCache = new CapabilityCache();
    }

    protected final void addCapabilityResolvers(List<ICapabilityHandlerManager<?, ?>> capabilityHandlerManagers) {
        for (var capabilityHandlerManager : capabilityHandlerManagers) {
            capabilityCache.addCapabilityManager(capabilityHandlerManager);
        }
    }

    protected <T> void addCapabilityResolver(BlockCapability<T, @Nullable Direction> capability,
                                             ICapabilityResolver<T, @Nullable Direction> provider) {
        capabilityCache.addCapabilityResolver(capability, provider);
    }

    public void invalidateCapabilitiesFull() {
        capabilityCache.invalidateAll();
        invalidateCapabilities();
    }

    @Override
    public void setRemoved() {
        capabilityCache.invalidateAll();
        super.setRemoved();
    }

    @Override
    public void clearRemoved() {
        capabilityCache.invalidateAll();
        super.clearRemoved();
    }

    public final void invalidateCapability(@NotNull BlockCapability<?, @Nullable Direction> capability, @Nullable Direction side) {
        capabilityCache.invalidate(capability, side);
        invalidateCapabilities();
    }

    public final void invalidateCapabilityAll(@NotNull BlockCapability<?, @Nullable Direction> capability) {
        capabilityCache.invalidateAll(capability);
        invalidateCapabilities();
    }

    public final void invalidateCapabilities(@NotNull Collection<BlockCapability<?, @Nullable Direction>> capabilities, @Nullable Direction side) {
        for (BlockCapability<?, @Nullable Direction> capability : capabilities) {
            capabilityCache.invalidate(capability, side);
        }
        invalidateCapabilities();
    }

    public final void invalidateCapabilitiesAll(@NotNull Collection<BlockCapability<?, @Nullable Direction>> capabilities) {
        for (BlockCapability<?, @Nullable Direction> capability : capabilities) {
            capabilityCache.invalidateAll(capability);
        }
        invalidateCapabilities();
    }
}
