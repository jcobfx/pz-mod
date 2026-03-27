package pl.pzmod.attachments.containers.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;

public interface IFluidContainer extends IFluidTank {
    void setFluid(@NotNull FluidStack fluid);

    @Override
    default int getFluidAmount() {
        return getFluid().getAmount();
    }

    default boolean isEmpty() {
        return getFluid().isEmpty();
    }

    default boolean isFluidEqual(@NotNull FluidStack fluid) {
        return FluidStack.isSameFluidSameComponents(getFluid(), fluid);
    }

    /**
     *
     * @param stack          fluid to insert
     * @param action         action to perform
     * @param automationType automation type
     * @return fluid that could not be inserted
     */
    default FluidStack insert(@NotNull FluidStack stack, @NotNull Action action, @NotNull AutomationType automationType) {
        if (stack.isEmpty() || !isFluidValid(stack)) {
            return stack;
        }
        int needed = Math.max(0, getCapacity() - getFluidAmount());
        if (needed == 0) {
            return stack;
        }
        var current = getFluid();
        boolean empty = stack.isEmpty();
        boolean sameType = !empty && FluidStack.isSameFluidSameComponents(stack, current);
        if (empty || sameType) {
            int toAdd = Math.min(stack.getAmount(), needed);
            if (action.execute()) {
                if (sameType) {
                    setFluid(current.copyWithAmount(current.getAmount() + toAdd));
                } else {
                    setFluid(stack.copyWithAmount(toAdd));
                }
            }
            return stack.copyWithAmount(stack.getAmount() - toAdd);
        }
        return stack;
    }

    /**
     *
     * @param amount         amount of fluid to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return extracted fluid
     */
    default @NotNull FluidStack extract(int amount, @NotNull Action action, @NotNull AutomationType automationType) {
        if (isEmpty() || amount < 1) {
            return FluidStack.EMPTY;
        }
        FluidStack current = getFluid();
        FluidStack ret = current.copyWithAmount(Math.min(current.getAmount(), amount));
        if (!ret.isEmpty() && action.execute()) {
            setFluid(current.copyWithAmount(current.getAmount() - ret.getAmount()));
        }
        return ret;
    }

    @Override
    default int fill(@NotNull FluidStack stack, IFluidHandler.@NotNull FluidAction action) {
        return stack.getAmount() - insert(stack, Action.fromFluidAction(action), AutomationType.EXTERNAL).getAmount();
    }

    @Override
    default @NotNull FluidStack drain(int amount, IFluidHandler.@NotNull FluidAction action) {
        return extract(amount, Action.fromFluidAction(action), AutomationType.EXTERNAL);
    }

    @Override
    default @NotNull FluidStack drain(@NotNull FluidStack stack, IFluidHandler.@NotNull FluidAction action) {
        if (!isEmpty() && isFluidEqual(stack)) {
            return extract(stack.getAmount(), Action.fromFluidAction(action), AutomationType.EXTERNAL);
        }
        return FluidStack.EMPTY;
    }
}
