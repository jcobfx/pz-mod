package pl.pzmod.capabilities.fluid;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;

public interface ISidedFluidHandler extends IExtendedFluidHandler {
    int getTanks(@Nullable Direction side);

    @NotNull
    FluidStack getFluidInTank(int tank, @Nullable Direction side);

    void setFluidInTank(int tank, @NotNull FluidStack stack, @Nullable Direction side);

    int getTankCapacity(int tank, @Nullable Direction side);

    boolean isFluidValid(int tank, @NotNull FluidStack stack, @Nullable Direction side);

    /**
     *
     * @param tank   index of tank
     * @param stack  fluid to insert
     * @param action action to perform
     * @param side   side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack insertFluid(int tank, @NotNull FluidStack stack, @NotNull Action action, @Nullable Direction side);

    /**
     *
     * @param tank   index of tank
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @param side   side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(int tank, int amount, @NotNull Action action, @Nullable Direction side);

    /**
     *
     * @param stack  fluid to insert
     * @param action action to perform
     * @param side   side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return fluid that could not be inserted
     */
    @NotNull
    FluidStack insertFluid(@NotNull FluidStack stack, @NotNull Action action, @Nullable Direction side);

    /**
     *
     * @param stack  fluid to extract
     * @param action action to perform
     * @param side   side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(@NotNull FluidStack stack, @NotNull Action action, @Nullable Direction side);

    /**
     *
     * @param amount amount of fluid to extract
     * @param action action to perform
     * @param side   side from which the extraction is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return extracted fluid
     */
    @NotNull
    FluidStack extractFluid(int amount, @NotNull Action action, @Nullable Direction side);

    default @Nullable Direction getFluidHandlerSideFor() {
        return null;
    }

    @Override
    default int getTanks() {
        return getTanks(getFluidHandlerSideFor());
    }

    @Override
    default @NotNull FluidStack getFluidInTank(int tank) {
        return getFluidInTank(tank, getFluidHandlerSideFor());
    }

    @Override
    default void setFluidInTank(int tank, @NotNull FluidStack stack) {
        setFluidInTank(tank, stack, getFluidHandlerSideFor());
    }

    @Override
    default int getTankCapacity(int tank) {
        return getTankCapacity(tank, getFluidHandlerSideFor());
    }

    @Override
    default boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return isFluidValid(tank, stack, getFluidHandlerSideFor());
    }

    @Override
    default @NotNull FluidStack insertFluid(int tank, @NotNull FluidStack stack, @NotNull Action action) {
        return insertFluid(tank, stack, action, getFluidHandlerSideFor());
    }

    @Override
    default @NotNull FluidStack extractFluid(int tank, int amount, @NotNull Action action) {
        return extractFluid(tank, amount, action, getFluidHandlerSideFor());
    }

    @Override
    default @NotNull FluidStack insertFluid(@NotNull FluidStack stack, @NotNull Action action) {
        return insertFluid(stack, action, getFluidHandlerSideFor());
    }

    @Override
    default @NotNull FluidStack extractFluid(@NotNull FluidStack stack, @NotNull Action action) {
        return extractFluid(stack, action, getFluidHandlerSideFor());
    }

    @Override
    default @NotNull FluidStack extractFluid(int amount, @NotNull Action action) {
        return extractFluid(amount, action, getFluidHandlerSideFor());
    }
}
