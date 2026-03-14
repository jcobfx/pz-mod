package pl.pzmod.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities.FluidHandler;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class Capabilities {
    public static final Capability<IEnergyStorage> ENERGY =
            new Capability<>(EnergyStorage.BLOCK, EnergyStorage.ENTITY, EnergyStorage.ITEM);

    public static final ICapability<IFluidHandler, IFluidHandlerItem> FLUID =
            new FluidCapability(FluidHandler.BLOCK, FluidHandler.ENTITY, FluidHandler.ITEM);

    public static final Capability<IItemHandler> ITEM =
            new Capability<>(ItemHandler.BLOCK, ItemHandler.ENTITY, ItemHandler.ITEM);

    private Capabilities() {
    }

    private record FluidCapability(BlockCapability<IFluidHandler, @Nullable Direction> block,
                                   EntityCapability<IFluidHandler, @Nullable Direction> entity,
                                   ItemCapability<IFluidHandlerItem, Void> item) implements ICapability<IFluidHandler, IFluidHandlerItem> {
    }
}
