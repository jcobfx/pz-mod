package pl.pzmod.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.fluid.IFluidHolder;
import pl.pzmod.capabilities.item.IItemHolder;

import java.util.function.BiPredicate;

public abstract class PZItem extends Item implements IEnergyHolder, IFluidHolder, IItemHolder {
    protected PZItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnergyCapacity() {
        return 0;
    }

    @Override
    public int getEnergyMaxTransfer() {
        return 0;
    }

    @Override
    public int getTankCount() {
        return 0;
    }

    @Override
    public int getTankCapacity() {
        return 0;
    }

    @Override
    public BiPredicate<Integer, @NotNull FluidStack> getFluidValidator() {
        return null;
    }

    @Override
    public int getSlotCount() {
        return 0;
    }

    @Override
    public int getSlotLimit() {
        return 0;
    }

    public @Nullable IEnergyStorage getEnergyStorage(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM);
    }

    public @Nullable IFluidHandler getFluidHandler(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    public @Nullable IItemHandler getItemHandler(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.ItemHandler.ITEM);
    }
}
