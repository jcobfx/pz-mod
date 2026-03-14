package pl.pzmod.data.containers.fluids;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;

public interface IFluidContainer {
    int getRate();

    int getCapacity();

    @NotNull FluidStack getFluid();

    void setFluid(@NotNull FluidStack fluid);

    boolean isFluidValid(@NotNull FluidStack fluid);

    boolean canInsert(@NotNull AutomationType automationType);

    boolean canExtract(@NotNull AutomationType automationType);

    default boolean isEmpty() {
        return getFluid().isEmpty();
    }

    default boolean isFluidEqual(@NotNull FluidStack fluid) {
        return FluidStack.isSameFluidSameComponents(getFluid(), fluid);
    }

    /**
     *
     * @param fluid          fluid to insert
     * @param action         action to perform
     * @param automationType automation type
     * @return fluid that could not be inserted
     */
    default FluidStack insert(@NotNull FluidStack fluid, @NotNull Action action, @NotNull AutomationType automationType) {
        var stored = getFluid();
        if (fluid.isEmpty() || !canInsert(automationType) || !isFluidValid(fluid)
                || (!stored.isEmpty() && !FluidStack.isSameFluidSameComponents(stored, fluid))) {
            return fluid;
        }
        int storedAmount = stored.getAmount();
        int toInsert = fluid.getAmount();
        int inserted = Math.min(Math.min(getRate(), toInsert), getCapacity() - storedAmount);
        if (inserted > 0 && action.execute()) {
            setFluid(fluid.copyWithAmount(storedAmount + inserted));
        }
        return fluid.copyWithAmount(toInsert - inserted);
    }

    /**
     *
     * @param fluid          fluid to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return extracted fluid
     */
    default @NotNull FluidStack extract(@NotNull FluidStack fluid, @NotNull Action action, @NotNull AutomationType automationType) {
        return !fluid.isEmpty() && isFluidEqual(fluid) ? extract(fluid.getAmount(), action, automationType) : fluid;
    }

    /**
     *
     * @param amount         amount of fluid to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return extracted fluid
     */
    default @NotNull FluidStack extract(int amount, @NotNull Action action, @NotNull AutomationType automationType) {
        if (amount <= 0 || !canExtract(automationType)) {
            return FluidStack.EMPTY;
        }
        var stored = getFluid();
        int storedAmount = stored.getAmount();
        int extracted = Math.min(Math.min(getRate(), amount), storedAmount);
        if (extracted > 0 && action.execute()) {
            setFluid(stored.copyWithAmount(storedAmount - extracted));
        }
        return stored.copyWithAmount(extracted);
    }
}
