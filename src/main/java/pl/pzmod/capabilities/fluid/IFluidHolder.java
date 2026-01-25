package pl.pzmod.capabilities.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

public interface IFluidHolder {
    int getTanks();

    int getCapacity();

    BiPredicate<Integer, @NotNull FluidStack> getValidator();
}
