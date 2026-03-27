package pl.pzmod.capabilities.fluid;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.fluid.IFluidContainer;

import java.util.List;
import java.util.Optional;

public interface IPZFluidHandler extends ISidedFluidHandler {
    @NotNull
    List<IFluidContainer> getFluidContainers(@Nullable Direction side);

    default Optional<IFluidContainer> getFluidContainer(int tank, @Nullable Direction side) {
        var fluidContainers = getFluidContainers(side);
        return Optional.ofNullable(tank >= 0 && tank < fluidContainers.size() ? fluidContainers.get(tank) : null);
    }

    default boolean hasFluidContainers() {
        return true;
    }

    @Override
    default int getTanks(@Nullable Direction side) {
        return getFluidContainers(side).size();
    }

    @Override
    default @NotNull FluidStack getFluidInTank(int tank, @Nullable Direction side) {
        return getFluidContainer(tank, side).map(IFluidContainer::getFluid).orElse(FluidStack.EMPTY);
    }

    @Override
    default void setFluidInTank(int tank, @NotNull FluidStack stack, @Nullable Direction side) {
        getFluidContainer(tank, side).ifPresent(c -> c.setFluid(stack));
    }

    @Override
    default int getTankCapacity(int tank, @Nullable Direction side) {
        return getFluidContainer(tank, side).map(IFluidContainer::getCapacity).orElse(0);
    }

    @Override
    default boolean isFluidValid(int tank, @NotNull FluidStack stack, @Nullable Direction side) {
        return getFluidContainer(tank, side).map(c -> c.isFluidEqual(stack)).orElse(false);
    }

    @Override
    @NotNull
    default FluidStack insertFluid(int tank, @NotNull FluidStack stack, @NotNull Action action, @Nullable Direction side) {
        return getFluidContainer(tank, side).map(c -> c.insert(stack, action, AutomationType.handler(side))).orElse(stack);
    }

    @Override
    @NotNull
    default FluidStack extractFluid(int tank, int amount, @NotNull Action action, @Nullable Direction side) {
        return getFluidContainer(tank, side).map(c -> c.extract(amount, action, AutomationType.handler(side))).orElse(FluidStack.EMPTY);
    }

    @Override
    default @NotNull FluidStack insertFluid(@NotNull FluidStack stack, @NotNull Action action, @Nullable Direction side) {
        return ExtendedFluidHandlerUtils.insertFluid(this::getFluidContainers, stack, side, action);
    }

    @Override
    default @NotNull FluidStack extractFluid(@NotNull FluidStack stack, @NotNull Action action, @Nullable Direction side) {
        return ExtendedFluidHandlerUtils.extractFluid(this::getFluidContainers, stack, side, action);
    }

    @Override
    default @NotNull FluidStack extractFluid(int amount, @NotNull Action action, @Nullable Direction side) {
        return ExtendedFluidHandlerUtils.extractFluid(this::getFluidContainers, amount, side, action);
    }
}
