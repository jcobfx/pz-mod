package pl.pzmod.data.containers.fluids;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.Action;

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
    FluidStack insert(int tank, @NotNull FluidStack stack, @NotNull Action action);

    /**
     *
     * @param tank   index of tank
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack extract(int tank, int amount, @NotNull Action action);

    /**
     *
     * @param stack  fluid to insert
     * @param action action to perform
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack insert(@NotNull FluidStack stack, @NotNull Action action);

    /**
     *
     * @param stack  fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @NotNull
    FluidStack extract(@NotNull FluidStack stack, @NotNull Action action);

    /**
     *
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @NotNull
    FluidStack extract(int amount, @NotNull Action action);

    /**
     *
     * @param stack  fluid to insert
     * @param action action to perform
     * @return amount of fluid inserted
     */
    @Override
    default int fill(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return stack.getAmount() - insert(stack, Action.fromFluidAction(action)).getAmount();
    }

    /**
     *
     * @param stack  fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @Override
    default @NotNull FluidStack drain(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return extract(stack, Action.fromFluidAction(action));
    }

    /**
     *
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @Override
    default @NotNull FluidStack drain(int amount, @NotNull FluidAction action) {
        return extract(amount, Action.fromFluidAction(action));
    }
}
