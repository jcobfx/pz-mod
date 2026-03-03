package pl.pzmod.capabilities.fluid;

import net.minecraft.core.NonNullList;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.CapabilityResolver;
import pl.pzmod.capabilities.proxy.Proxy;
import pl.pzmod.data.containers.AttachedFluids;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FluidCapabilityResolver<P, T, C> extends CapabilityResolver<Proxy<P>, T, C> implements IFluidHandler {
    private final int tankCount;
    private final int tankCapacity;
    private final BiPredicate<Integer, FluidStack> fluidValidator;

    protected FluidCapabilityResolver(Proxy<P> dataHolder,
                                      Supplier<T> dataType,
                                      C context,
                                      Predicate<C> canFill,
                                      Predicate<C> canDrain) {
        super(dataHolder, dataType, context, canFill, canDrain);
        this.tankCount = dataHolder.getTankCount();
        this.tankCapacity = dataHolder.getTankCapacity();
        this.fluidValidator = dataHolder.getFluidValidator();
    }

    @Override
    public int getTanks() {
        return tankCount;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        AttachedFluids contents = getContents();
        return getStackFromContents(contents, tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return tankCapacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluidValidator.test(tank, stack);
    }

    @Override
    public int fill(@NotNull FluidStack toFill, @NotNull FluidAction action) {
        Optional<Integer> tankOpt = findFirstValidTank(toFill);
        if (tankOpt.isEmpty() || !canInsert()) {
            return 0;
        }

        int tank = tankOpt.get();
        FluidStack existing = getFluidInTank(tank);
        int fillLimit = getTankCapacity(tank);
        if (!existing.isEmpty()) {
            fillLimit -= existing.getAmount();
        }
        if (fillLimit <= 0) {
            return 0;
        }

        int filled = Math.min(fillLimit, toFill.getAmount());
        if (!action.simulate()) {
            FluidStack newStack;
            if (existing.isEmpty()) {
                newStack = toFill.copyWithAmount(filled);
            } else {
                newStack = existing.copyWithAmount(existing.getAmount() + filled);
            }
            updateContents(getContents(), newStack, tank);
        }
        return filled;
    }

    @Override
    public @NotNull FluidStack drain(@NotNull FluidStack toDrain, @NotNull FluidAction action) {
        Optional<Integer> tankOpt = findFirstDrainableTank(toDrain);
        if (tankOpt.isEmpty() || !canExtract()) {
            return FluidStack.EMPTY;
        }

        int tank = tankOpt.get();
        FluidStack existing = getFluidInTank(tank);
        int toExtract = Math.min(existing.getAmount(), toDrain.getAmount());
        if (toExtract <= 0) {
            return FluidStack.EMPTY;
        }
        if (!action.simulate()) {
            FluidStack newStack = existing.copyWithAmount(existing.getAmount() - toExtract);
            updateContents(getContents(), newStack, tank);
        }
        return existing.copyWithAmount(toExtract);
    }

    @Override
    public @NotNull FluidStack drain(int tank, @NotNull FluidAction action) {
        validateTankIndex(tank);
        FluidStack existing = getFluidInTank(tank);
        if (existing.isEmpty() || !canExtract()) {
            return FluidStack.EMPTY;
        }

        int toExtract = existing.getAmount();
        if (!action.simulate()) {
            updateContents(getContents(), FluidStack.EMPTY, tank);
        }
        return existing.copyWithAmount(toExtract);
    }

    protected void onContentsChanged(int slot, FluidStack oldStack, FluidStack newStack) {
        // Do nothing by default, can be overridden to react to changes in the fluid contents
    }

    private AttachedFluids getContents() {
        return getData(AttachedFluids.EMPTY);
    }

    private FluidStack getStackFromContents(@NotNull AttachedFluids contents, int tank) {
        this.validateTankIndex(tank);
        return contents.getTanks() <= tank ? FluidStack.EMPTY : contents.getFluidInTank(tank);
    }

    private void updateContents(@NotNull AttachedFluids contents, FluidStack stack, int tank) {
        validateTankIndex(tank);
        NonNullList<FluidStack> list = NonNullList.withSize(Math.max(contents.getTanks(), getTanks()), FluidStack.EMPTY);
        contents.copyInto(list);
        FluidStack oldStack = list.get(tank);
        list.set(tank, stack);
        setData(new AttachedFluids(list));
        onContentsChanged(tank, oldStack, stack);
    }

    private final void validateTankIndex(int tank) {
        if (tank < 0 || tank >= getTanks()) {
            throw new IndexOutOfBoundsException("Tank " + tank + " not in valid range - [0," + getTanks() + ")");
        }
    }

    private Optional<Integer> findFirstValidTank(@NotNull FluidStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        for (int i = 0; i < getTanks(); i++) {
            FluidStack existing = getFluidInTank(i);
            if (isFluidValid(i, stack) && existing.isEmpty() || FluidStack.isSameFluidSameComponents(stack, existing)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> findFirstDrainableTank(@NotNull FluidStack stack) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }

        for (int i = 0; i < getTanks(); i++) {
            FluidStack existing = getFluidInTank(i);
            if (FluidStack.isSameFluidSameComponents(existing, stack)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }
}
