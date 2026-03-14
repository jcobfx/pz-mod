package pl.pzmod.data.containers.fluids;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;

public interface ISidedFluidHandler extends IFluidHandler {
    int getTanks(@Nullable Direction side);

    @NotNull
    FluidStack getFluidInTank(int tank, @Nullable Direction side);

    int getTankCapacity(int tank, @Nullable Direction side);

    boolean isFluidValid(int tank, @NotNull FluidStack stack, @Nullable Direction side);

    /**
     *
     * @param stack  fluid to insert
     * @param side   side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @param action action to perform
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack insertFluid(@NotNull FluidStack stack, @Nullable Direction side, @NotNull Action action);

    /**
     *
     * @param stack  fluid to extract
     * @param side   side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @param action action to perform
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(@NotNull FluidStack stack, @Nullable Direction side, @NotNull Action action);

    /**
     *
     * @param amount amount of fluid to extract
     * @param side   side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @param action action to perform
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(int amount, @Nullable Direction side, @NotNull Action action);

    default @Nullable Direction getDefaultSide() {
        return null;
    }

    @Override
    default int getTanks() {
        return getTanks(getDefaultSide());
    }

    @Override
    default @NotNull FluidStack getFluidInTank(int tank) {
        return getFluidInTank(tank, getDefaultSide());
    }

    @Override
    default int getTankCapacity(int tank) {
        return getTankCapacity(tank, getDefaultSide());
    }

    @Override
    default boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return isFluidValid(tank, stack, getDefaultSide());
    }

    /**
     *
     * @param stack  fluid to insert
     * @param action action to perform
     * @return amount of fluid inserted
     */
    @Override
    default int fill(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return stack.getAmount() - insertFluid(stack, getDefaultSide(), Action.fromFluidAction(action)).getAmount();
    }

    /**
     *
     * @param stack  fluid to extract
     * @param action action to perform
     * @return extracted fluid
     */
    @Override
    default @NotNull FluidStack drain(@NotNull FluidStack stack, @NotNull FluidAction action) {
        return extractFluid(stack, getDefaultSide(), Action.fromFluidAction(action));
    }

    @Override
    default @NotNull FluidStack drain(int amount, @NotNull FluidAction action) {
        return extractFluid(amount, getDefaultSide(), Action.fromFluidAction(action));
    }
}
