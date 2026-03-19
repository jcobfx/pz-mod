package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.blocks.IEntityBlock;
import pl.pzmod.capabilities.holder.energy.IEnergyHolder;
import pl.pzmod.capabilities.holder.fluid.IFluidHolder;
import pl.pzmod.capabilities.holder.item.IItemHolder;
import pl.pzmod.capabilities.resolver.manager.EnergyHandlerManager;
import pl.pzmod.capabilities.resolver.manager.FluidHandlerManager;
import pl.pzmod.capabilities.resolver.manager.ICapabilityHandlerManager;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.capabilities.resolver.manager.ItemHandlerManager;
import pl.pzmod.capabilities.energy.IPZEnergyHandler;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.capabilities.fluid.IPZFluidHandler;
import pl.pzmod.capabilities.item.IItemContainer;
import pl.pzmod.capabilities.item.IPZItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PZBlockEntity extends CapabilityBlockEntity implements IPZEnergyHandler, IPZFluidHandler, IPZItemHandler {
    private final Holder<? extends Block> block;

    private final EnergyHandlerManager energyHandlerManager;
    private final FluidHandlerManager fluidHandlerManager;
    private final ItemHandlerManager itemHandlerManager;

    protected PZBlockEntity(Holder<? extends Block> blockProvider, BlockPos pos, BlockState blockState) {
        super(((IEntityBlock<?>) blockProvider.value()).getBlockEntityType().get(), pos, blockState);
        this.block = blockProvider;

        List<ICapabilityHandlerManager<?, ?>> capabilityManagers = new ArrayList<>();
        var energyHolder = getInitialEnergyHolder();
        if (energyHolder != null) {
            energyHandlerManager = new EnergyHandlerManager(energyHolder, this);
            capabilityManagers.add(energyHandlerManager);
        } else {
            energyHandlerManager = null;
        }
        var fluidHolder = getInitialFluidHolder();
        if (fluidHolder != null) {
            fluidHandlerManager = new FluidHandlerManager(fluidHolder, this);
            capabilityManagers.add(fluidHandlerManager);
        } else {
            fluidHandlerManager = null;
        }
        var itemHolder = getInitialItemHolder();
        if (itemHolder != null) {
            itemHandlerManager = new ItemHandlerManager(itemHolder, this);
            capabilityManagers.add(itemHandlerManager);
        } else {
            itemHandlerManager = null;
        }
        addCapabilityResolvers(capabilityManagers);
    }

    public Holder<? extends Block> getBlockHolder() {
        return block;
    }

    protected @NotNull Direction getFacing() {
        return getBlockState().getValue(BlockStateProperties.FACING);
    }

    protected @Nullable IEnergyHolder getInitialEnergyHolder() {
        return null;
    }

    protected @Nullable IFluidHolder getInitialFluidHolder() {
        return null;
    }

    protected @Nullable IItemHolder getInitialItemHolder() {
        return null;
    }

    @Override
    public @NotNull List<IEnergyContainer> getEnergyContainers(@Nullable Direction side) {
        return energyHandlerManager == null ? Collections.emptyList() : energyHandlerManager.getContainers(side);
    }

    @Override
    public boolean hasEnergyContainers() {
        return energyHandlerManager != null;
    }

    @Override
    public @NotNull List<IFluidContainer> getFluidContainers(@Nullable Direction side) {
        return fluidHandlerManager == null ? Collections.emptyList() : fluidHandlerManager.getContainers(side);
    }

    @Override
    public boolean hasFluidContainers() {
        return fluidHandlerManager != null;
    }

    @Override
    public @NotNull List<IItemContainer> getItemContainers(@Nullable Direction side) {
        return itemHandlerManager == null ? Collections.emptyList() : itemHandlerManager.getContainers(side);
    }

    @Override
    public boolean hasItemContainers() {
        return itemHandlerManager != null;
    }
}
