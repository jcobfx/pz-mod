package pl.pzmod.items;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.data.containers.energy.IPZEnergyHandler;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.data.containers.fluids.IPZFluidHandler;
import pl.pzmod.capabilities.item.IItemContainer;
import pl.pzmod.data.containers.items.IPZItemHandler;

import java.util.List;

public abstract class PZItem extends Item implements IPZEnergyHandler, IPZFluidHandler, IPZItemHandler {
    public static final ICapabilityProvider<ItemStack, Void, IEnergyStorage> ENERGY_HANDLER_PROVIDER;
    public static final ICapabilityProvider<ItemStack, Void, IFluidHandlerItem> FLUID_HANDLER_PROVIDER;
    public static final ICapabilityProvider<ItemStack, Void, IItemHandler> ITEM_HANDLER_PROVIDER;

    static {
        ENERGY_HANDLER_PROVIDER = null;
        FLUID_HANDLER_PROVIDER = null;
        ITEM_HANDLER_PROVIDER = null;
    }

    protected PZItem(Properties properties) {
        super(properties);
    }

    protected @Nullable EnergyHandler getInitialEnergyHandler(@NotNull ItemStack stack) {
        return null;
    }

    protected @Nullable FluidHandler getInitialFluidHandler(@NotNull ItemStack stack) {
        return null;
    }

    protected @Nullable ItemHandler getInitialItemHandler(@NotNull ItemStack stack) {
        return null;
    }

    @Override
    public @NotNull List<IEnergyContainer> getEnergyContainers(@Nullable Direction side) {
        return List.of();
    }

    @Override
    public boolean hasEnergyContainers() {
        return false;
    }

    @Override
    public @NotNull List<IFluidContainer> getFluidContainers(@Nullable Direction side) {
        return List.of();
    }

    @Override
    public boolean hasFluidContainers() {
        return false;
    }

    @Override
    public @NotNull List<IItemContainer> getItemContainers(@Nullable Direction side) {
        return List.of();
    }

    @Override
    public boolean hasItemContainers() {
        return false;
    }
}
