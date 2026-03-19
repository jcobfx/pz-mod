package pl.pzmod.capabilities.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;

public interface IExtendedFluidHandler extends IFluidHandler {
    void setFluidInTank(int tank, @NotNull FluidStack stack);

    /**
     *
     * @param tank   index of tank
     * @param stack  fluid to insert
     * @param action action to perform
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack insertFluid(int tank, @NotNull FluidStack stack, @NotNull Action action);

    /**
     *
     * @param tank   index of tank
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack extractFluid(int tank, int amount, @NotNull Action action);

    /**
     *
     * @param stack  fluid to insert
     * @param action action to perform
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack insertFluid(@NotNull FluidStack stack, @NotNull Action action);

    /**
     *
     * @param stack  fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(@NotNull FluidStack stack, @NotNull Action action);

    /**
     *
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(int amount, @NotNull Action action);

    /**
     *
     * @param stack  fluid to insert
     * @param action action to perform
     * @return amount of fluid inserted
     */
    @Override
    default int fill(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return stack.getAmount() - insertFluid(stack, Action.fromFluidAction(action)).getAmount();
    }

    /**
     *
     * @param stack  fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @Override
    default @NotNull FluidStack drain(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return extractFluid(stack, Action.fromFluidAction(action));
    }

    /**
     *
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @Override
    default @NotNull FluidStack drain(int amount, @NotNull FluidAction action) {
        return extractFluid(amount, Action.fromFluidAction(action));
    }
}
