package pl.pzmod.capabilities.fluid;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.CapabilityResolver;
import pl.pzmod.data.containers.AttachedFluids;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static pl.pzmod.registries.PZDataComponents.ATTACHED_FLUIDS;

public class FluidCapabilityResolver<HOLDER extends MutableDataComponentHolder, CONTEXT>
        extends CapabilityResolver<HOLDER, CONTEXT, AttachedFluids> implements IFluidHandlerItem {
    private final int tanks;
    private final int capacity;
    private final BiPredicate<Integer, FluidStack> validator;

    protected FluidCapabilityResolver(HOLDER parent,
                                      CONTEXT context,
                                      Predicate<CONTEXT> canFill,
                                      Predicate<CONTEXT> canDrain,
                                      int tanks,
                                      int capacity,
                                      BiPredicate<Integer, @NotNull FluidStack> validator) {
        super(parent, ATTACHED_FLUIDS, context, canFill, canDrain);
        this.tanks = tanks;
        this.capacity = capacity;
        this.validator = validator;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return (ItemStack) getParent();
    }

    @Override
    public int getTanks() {
        return tanks;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        AttachedFluids contents = getContents();
        return getStackFromContents(contents, tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return validator.test(tank, stack);
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
    }

    protected AttachedFluids getContents() {
        return getAttached(AttachedFluids.EMPTY);
    }

    protected FluidStack getStackFromContents(@NotNull AttachedFluids contents, int tank) {
        this.validateTankIndex(tank);
        return contents.getTanks() <= tank ? FluidStack.EMPTY : contents.getFluidInTank(tank);
    }

    protected void updateContents(@NotNull AttachedFluids contents, FluidStack stack, int tank) {
        validateTankIndex(tank);
        NonNullList<FluidStack> list = NonNullList.withSize(Math.max(contents.getTanks(), getTanks()), FluidStack.EMPTY);
        contents.copyInto(list);
        FluidStack oldStack = list.get(tank);
        list.set(tank, stack);
        setAttached(new AttachedFluids(list));
        onContentsChanged(tank, oldStack, stack);
    }

    protected final void validateTankIndex(int tank) {
        if (tank < 0 || tank >= getTanks()) {
            throw new RuntimeException("Tank " + tank + " not in valid range - [0," + getTanks() + ")");
        }
    }

    protected Optional<Integer> findFirstValidTank(@NotNull FluidStack stack) {
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

    protected Optional<Integer> findFirstDrainableTank(@NotNull FluidStack stack) {
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
