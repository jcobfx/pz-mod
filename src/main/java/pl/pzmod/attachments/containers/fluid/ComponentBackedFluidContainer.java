package pl.pzmod.attachments.containers.fluid;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.attachments.containers.ComponentBackedContainer;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.fluid.IFluidContainer;

import java.util.function.BiPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class ComponentBackedFluidContainer extends ComponentBackedContainer<FluidStack, AttachedFluids> implements IFluidContainer {
    private final BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canExtract;
    private final BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canInsert;
    private final Predicate<@NotNull FluidStack> validator;
    private final IntSupplier capacity;
    private final IntSupplier rate;

    public ComponentBackedFluidContainer(ItemStack attachedTo,
                                         int index,
                                         BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canInsert,
                                         BiPredicate<@NotNull FluidStack, @NotNull AutomationType> canExtract,
                                         Predicate<@NotNull FluidStack> validator,
                                         IntSupplier capacity,
                                         IntSupplier rate) {
        super(attachedTo, index);
        this.validator = validator;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.capacity = capacity;
        this.rate = rate;
    }

    @Override
    protected @NotNull FluidStack copy(FluidStack toCopy) {
        return toCopy.copy();
    }

    @Override
    protected @NotNull ContainerType<?, AttachedFluids, ?> getContainerType() {
        return ContainerType.FLUIDS;
    }

    @Override
    public void setFluid(@NotNull FluidStack fluid) {
        setContents(getAttached(), fluid);
    }

    @Override
    public @NotNull FluidStack getFluid() {
        return getContents(getAttached());
    }

    @Override
    public int getCapacity() {
        return capacity.getAsInt();
    }

    @Override
    public boolean isFluidValid(@NotNull FluidStack fluid) {
        return validator.test(fluid);
    }

    protected int getNeeded(@NotNull FluidStack stack) {
        return Math.max(0, getCapacity() - stack.getAmount());
    }

    protected int getInsertRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : rate.getAsInt();
    }

    protected int getExtractRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : rate.getAsInt();
    }

    @Override
    public FluidStack insert(@NotNull FluidStack stack, @NotNull Action action, @NotNull AutomationType automationType) {
        if (stack.isEmpty() || !isFluidValid(stack) || !canInsert.test(stack, automationType)) {
            return stack;
        }
        AttachedFluids attachedFluids = getAttached();
        FluidStack stored = getContents(attachedFluids);
        int needed = Math.min(getInsertRate(automationType), getNeeded(stored));
        if (needed <= 0) {
            return stack;
        } else if (stored.isEmpty() || FluidStack.isSameFluidSameComponents(stored, stack)) {
            int toAdd = Math.min(stack.getAmount(), needed);
            if (action.execute()) {
                setContents(attachedFluids, stack.copyWithAmount(stored.getAmount() + toAdd));
            }
            return stack.copyWithAmount(stack.getAmount() - toAdd);
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
        if (stored.isEmpty() || !canExtract.test(stored, automationType)) {
            return FluidStack.EMPTY;
        }
        int size = Math.min(Math.min(getExtractRate(automationType), stored.getAmount()), amount);
        FluidStack ret = stored.copyWithAmount(size);
        if (!ret.isEmpty() && action.execute()) {
            setContents(attachedFluids, stored.copyWithAmount(stored.getAmount() - ret.getAmount()));
        }
        return ret;
    }
}
