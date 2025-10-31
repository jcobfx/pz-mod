package pl.pzmod.items;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.fluid.IFluidHolder;
import pl.pzmod.capabilities.item.IItemHolder;

import java.util.function.BiPredicate;

public abstract class PZItem extends Item implements IEnergyHolder, IItemHolder, IFluidHolder {
    public PZItem(Properties properties) {
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
    public int getSlots() {
        return 0;
    }

    @Override
    public int getLimit() {
        return 0;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public BiPredicate<Integer, @NotNull FluidStack> getValidator() {
        return null;
    }
}
