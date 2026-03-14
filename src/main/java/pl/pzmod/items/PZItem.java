package pl.pzmod.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.fluids.IFluidHolder;
import pl.pzmod.capabilities.items.IItemHolder;
import pl.pzmod.data.containers.energy.EnergyHandler;
import pl.pzmod.data.containers.fluids.FluidHandler;
import pl.pzmod.data.containers.items.ItemHandler;

public abstract class PZItem extends Item implements IEnergyHolder<ItemStack>, IFluidHolder<ItemStack>, IItemHolder<ItemStack> {
    private EnergyHandler energyHandler;
    private FluidHandler fluidHandler;
    private ItemHandler itemHandler;

    protected PZItem(Properties properties) {
        super(properties);
    }

    protected @Nullable EnergyHandler getInitialEnergyHandler(@NotNull ItemStack stack) {
        return null;
    }

    @Override
    public final @Nullable EnergyHandler getEnergyHandler(@NotNull ItemStack stack) {
        if (energyHandler == null) {
            energyHandler = getInitialEnergyHandler(stack);
        }
        return energyHandler;
    }

    @Override
    public boolean canHandleEnergy() {
        return false;
    }

    protected @Nullable FluidHandler getInitialFluidHandler(@NotNull ItemStack stack) {
        return null;
    }

    @Override
    public final @Nullable FluidHandler getFluidHandler(@NotNull ItemStack stack) {
        if (fluidHandler == null) {
            fluidHandler = getInitialFluidHandler(stack);
        }
        return fluidHandler;
    }

    @Override
    public boolean canHandleFluids() {
        return false;
    }

    protected @Nullable ItemHandler getInitialItemHandler(@NotNull ItemStack stack) {
        return null;
    }

    @Override
    public final @Nullable ItemHandler getItemHandler(@NotNull ItemStack stack) {
        if (itemHandler == null) {
            itemHandler = getInitialItemHandler(stack);
        }
        return itemHandler;
    }

    @Override
    public boolean canHandleItems() {
        return false;
    }
}
