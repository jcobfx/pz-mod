package pl.pzmod.data.containers.fluids;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class ExtendedFluidHandlerUtils {
    public static @NotNull FluidStack insertFluid(Function<@Nullable Direction, List<IFluidContainer>> containersGetter,
                                                  @NotNull FluidStack stack,
                                                  @Nullable Direction side,
                                                  @NotNull Action action) {
        var containers = containersGetter.apply(side);
        int size = containers.size();
        var automationType = AutomationType.handler(side);
        if (stack.isEmpty()) {
            return FluidStack.EMPTY;
        } else if (size == 0) {
            return stack;
        } else if (size == 1) {
            return containers.getFirst().insert(stack, action, automationType);
        }
        FluidStack toInsert = stack;
        List<IFluidContainer> emptyContainers = new ArrayList<>();
        for (IFluidContainer fluidContainer : containers) {
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

    public static @NotNull FluidStack extractFluid(Function<@Nullable Direction, List<IFluidContainer>> containersGetter,
                                                   @NotNull FluidStack stack,
                                                   @Nullable Direction side,
                                                   @NotNull Action action) {
        var containers = containersGetter.apply(side);
        int size = containers.size();
        var automationType = AutomationType.handler(side);
        if (stack.isEmpty() || size == 0) {
            return FluidStack.EMPTY;
        } else if (size == 1) {
            var fluidContainer = containers.getFirst();
            if (fluidContainer.isEmpty() || !fluidContainer.isFluidEqual(stack)) {
                return FluidStack.EMPTY;
            }
            return fluidContainer.extract(stack.getAmount(), action, automationType);
        }
        return extractFluidHelper(containers, stack.getAmount(), action, automationType, stack);
    }

    public static @NotNull FluidStack extractFluid(Function<@Nullable Direction, List<IFluidContainer>> containersGetter,
                                                   int amount,
                                                   @Nullable Direction side,
                                                   @NotNull Action action) {
        var containers = containersGetter.apply(side);
        int size = containers.size();
        var automationType = AutomationType.handler(side);
        if (amount == 0 || size == 0) {
            return FluidStack.EMPTY;
        } else if (size == 1) {
            return containers.getFirst().extract(amount, action, automationType);
        }
        return extractFluidHelper(containers, amount, action, automationType, null);
    }

    private static @NotNull FluidStack extractFluidHelper(List<IFluidContainer> containers,
                                                          int amount,
                                                          @NotNull Action action,
                                                          @NotNull AutomationType automationType,
                                                          @Nullable FluidStack expected) {
        int toDrain = amount;
        FluidStack extracted = FluidStack.EMPTY;
        for (IFluidContainer fluidContainer : containers) {
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

    private ExtendedFluidHandlerUtils() {
    }
}
