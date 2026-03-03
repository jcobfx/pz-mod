package pl.pzmod.capabilities.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;

public interface IFluidHolder {
    int getTankCount();

    int getTankCapacity();

    BiPredicate<Integer, @NotNull FluidStack> getFluidValidator();
}
