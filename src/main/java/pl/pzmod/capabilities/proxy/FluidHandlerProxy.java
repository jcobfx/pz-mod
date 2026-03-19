package pl.pzmod.capabilities.proxy;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.fluid.IExtendedFluidHandler;
import pl.pzmod.capabilities.fluid.ISidedFluidHandler;

public class FluidHandlerProxy implements IExtendedFluidHandler {
    private final ISidedFluidHandler sidedFluidHandler;
    private final @Nullable Direction side;

    public FluidHandlerProxy(ISidedFluidHandler sidedFluidHandler, @Nullable Direction side) {
        this.sidedFluidHandler = sidedFluidHandler;
        this.side = side;
    }

    @Override
    public int getTanks() {
        return sidedFluidHandler.getTanks(side);
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return sidedFluidHandler.getFluidInTank(tank, side);
    }

    @Override
    public int getTankCapacity(int tank) {
        return sidedFluidHandler.getTankCapacity(tank, side);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return sidedFluidHandler.isFluidValid(tank, stack, side);
    }

    @Override
    public void setFluidInTank(int tank, @NotNull FluidStack stack) {
        sidedFluidHandler.setFluidInTank(tank, stack, side);
    }

    @Override
    public @NotNull FluidStack insertFluid(int tank, @NotNull FluidStack stack, @NotNull Action action) {
        return sidedFluidHandler.insertFluid(tank, stack, action, side);
    }

    @Override
    public @NotNull FluidStack extractFluid(int tank, int amount, @NotNull Action action) {
        return sidedFluidHandler.extractFluid(tank, amount, action, side);
    }

    @Override
    public @NotNull FluidStack insertFluid(@NotNull FluidStack stack, @NotNull Action action) {
        return sidedFluidHandler.insertFluid(stack, action, side);
    }

    @Override
    public @NotNull FluidStack extractFluid(@NotNull FluidStack stack, @NotNull Action action) {
        return sidedFluidHandler.extractFluid(stack, action, side);
    }

    @Override
    public @NotNull FluidStack extractFluid(int amount, @NotNull Action action) {
        return sidedFluidHandler.extractFluid(amount, action, side);
    }
}
