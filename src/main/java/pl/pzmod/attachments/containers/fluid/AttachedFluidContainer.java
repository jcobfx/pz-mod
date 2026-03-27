package pl.pzmod.attachments.containers.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.AttachedContainer;

import java.util.function.*;

public class AttachedFluidContainer extends AttachedContainer<FluidStack, AttachedFluids> implements IFluidContainer {
    private final Predicate<@NotNull FluidStack> validator;
    private final BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canInsert;
    private final BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canExtract;
    private final IntSupplier capacity;
    private final IntSupplier rate;

    public AttachedFluidContainer(int index,
                                  Supplier<@NotNull AttachedFluids> attachedGetter,
                                  Consumer<@NotNull AttachedFluids> attachedSetter,
                                  Supplier<@NotNull AttachedFluids> attachedCreator,
                                  Predicate<@NotNull FluidStack> validator,
                                  BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canInsert,
                                  BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canExtract,
                                  IntSupplier capacity,
                                  IntSupplier rate) {
        super(index, attachedGetter, attachedSetter, attachedCreator);
        this.validator = validator;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.capacity = capacity;
        this.rate = rate;
    }

    public int getRate() {
        return rate.getAsInt();
    }

    public boolean canInsert(@NotNull FluidStack stack, @NotNull AutomationType automationType) {
        return canInsert.test(stack, automationType);
    }

    public boolean canExtract(@NotNull FluidStack stack, @NotNull AutomationType automationType) {
        return canExtract.test(stack, automationType);
    }

    @Override
    public int getCapacity() {
        return capacity.getAsInt();
    }

    @Override
    public boolean isFluidValid(@NotNull FluidStack fluidStack) {
        return validator.test(fluidStack);
    }

    @Override
    public @NotNull FluidStack getFluid() {
        return getContents(getAttached());
    }

    @Override
    public void setFluid(@NotNull FluidStack fluid) {
        setContents(getAttached(), fluid);
    }

    protected int getNeeded(@NotNull FluidStack stack) {
        return Math.max(0, getCapacity() - stack.getAmount());
    }

    protected int getInsertRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : getRate();
    }

    protected int getExtractRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : getRate();
    }

    @Override
    public FluidStack insert(@NotNull FluidStack stack, @NotNull Action action, @NotNull AutomationType automationType) {
        if (stack.isEmpty() || !isFluidValid(stack) || !canInsert(stack, automationType)) {
            return stack;
        }
        AttachedFluids attachedFluids = getAttached();
        FluidStack stored = getContents(attachedFluids);
        int needed = Math.min(getInsertRate(automationType), getNeeded(stored));
        if (needed <= 0) {
            return stack;
        } else if (stored.isEmpty() || FluidStack.isSameFluidSameComponents(stored, stack)) {
            int toInsert = Math.min(stack.getAmount(), needed);
            if (action.execute()) {
                setContents(attachedFluids, stack.copyWithAmount(stored.getAmount() + toInsert));
            }
            return stack.copyWithAmount(stack.getAmount() - toInsert);
        }
        return stack;
    }

    @Override
    public @NotNull FluidStack extract(int amount, @NotNull Action action, @NotNull AutomationType automationType) {
        if (amount < 1) {
            return FluidStack.EMPTY;
        }
        AttachedFluids attachedFluids = getAttached();
        FluidStack stored = getContents(attachedFluids);
        if (stored.isEmpty() || !canExtract(stored, automationType)) {
            return FluidStack.EMPTY;
        }
        int size = Math.min(Math.min(getExtractRate(automationType), stored.getAmount()), amount);
        FluidStack toExtract = stored.copyWithAmount(size);
        if (!toExtract.isEmpty() && action.execute()) {
            setContents(attachedFluids, stored.copyWithAmount(stored.getAmount() - toExtract.getAmount()));
        }
        return toExtract;
    }
}
