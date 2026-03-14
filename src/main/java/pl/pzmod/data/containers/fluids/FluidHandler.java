package pl.pzmod.data.containers.fluids;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.MutableContainerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FluidHandler extends MutableContainerHandler<IFluidContainer> implements ISidedFluidHandler {
    public FluidHandler() {
        this(null);
    }

    public FluidHandler(@Nullable Supplier<Direction> facing) {
        super(facing);
    }

    @Override
    public int getTanks(@Nullable Direction side) {
        return getContainers(side).size();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank, @Nullable Direction side) {
        return getContainers(side).get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank, @Nullable Direction side) {
        return getContainers(side).get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack, @Nullable Direction side) {
        return getContainers(side).get(tank).isFluidValid(stack);
    }

    @Override
    public @NotNull FluidStack insertFluid(@NotNull FluidStack stack, @Nullable Direction side, @NotNull Action action) {
        int size = size();
        var automationType = AutomationType.handler(side);
        if (stack.isEmpty()) {
            return FluidStack.EMPTY;
        } else if (size == 0) {
            return stack;
        } else if (size == 1) {
            return get(0).insert(stack, action, automationType);
        }
        FluidStack toInsert = stack;
        List<IFluidContainer> emptyContainers = new ArrayList<>();
        for (IFluidContainer fluidContainer : this) {
            if (fluidContainer.isEmpty()) {
                emptyContainers.add(fluidContainer);
            } else if (fluidContainer.isFluidEqual(stack)) {
                FluidStack remainder = fluidContainer.insert(toInsert, action, automationType);
                if (remainder.isEmpty()) {
                    return FluidStack.EMPTY;
                }
                toInsert = remainder;
            }
        }
        for (IFluidContainer fluidContainer : emptyContainers) {
            FluidStack remainder = fluidContainer.insert(toInsert, action, automationType);
            if (remainder.isEmpty()) {
                return FluidStack.EMPTY;
            }
            toInsert = remainder;
        }
        return toInsert;
    }

    @Override
    public @NotNull FluidStack extractFluid(@NotNull FluidStack stack, @Nullable Direction side, @NotNull Action action) {
        int size = size();
        var automationType = AutomationType.handler(side);
        if (stack.isEmpty() || size == 0) {
            return FluidStack.EMPTY;
        } else if (size == 1) {
            var fluidContainer = get(0);
            if (fluidContainer.isEmpty() || !fluidContainer.isFluidEqual(stack)) {
                return FluidStack.EMPTY;
            }
            return fluidContainer.extract(stack.getAmount(), action, automationType);
        }
        return extractFluidHelper(stack.getAmount(), action, automationType, stack);
    }

    @Override
    public @NotNull FluidStack extractFluid(int amount, @Nullable Direction side, @NotNull Action action) {
        int size = size();
        var automationType = AutomationType.handler(side);
        if (amount == 0 || size == 0) {
            return FluidStack.EMPTY;
        } else if (size == 1) {
            return get(0).extract(amount, action, automationType);
        }
        return extractFluidHelper(amount, action, automationType, null);
    }

    private @NotNull FluidStack extractFluidHelper(int amount,
                                                   @NotNull Action action,
                                                   @NotNull AutomationType automationType,
                                                   @Nullable FluidStack expected) {
        int toDrain = amount;
        FluidStack extracted = FluidStack.EMPTY;
        for (IFluidContainer fluidContainer : this) {
            var toMatch = expected != null ? expected : extracted;
            if (!toMatch.isEmpty() && !fluidContainer.isFluidEqual(toMatch)) {
                continue;
            }
            FluidStack drained = fluidContainer.extract(toDrain, action, automationType);
            if (!drained.isEmpty()) {
                if (extracted.isEmpty()) {
                    extracted = drained;
                } else {
                    extracted.grow(drained.getAmount());
                }
                toDrain -= drained.getAmount();
                if (toDrain == 0) {
                    break;
                }
            }
        }
        return extracted;
    }
}
