package pl.pzmod.attachments.containers.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.AttachedContainer;

import java.util.function.*;

public class AttachedItemContainer extends AttachedContainer<ItemStack, AttachedItems> implements IItemContainer {
    private final Predicate<@NotNull ItemStack> validator;
    private final BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert;
    private final BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract;
    private final IntSupplier limit;
    private final BooleanSupplier obeyStackLimit;

    public AttachedItemContainer(int index,
                                 Supplier<@NotNull AttachedItems> attachedGetter,
                                 Consumer<@NotNull AttachedItems> attachedSetter,
                                 Supplier<@NotNull AttachedItems> attachedCreator,
                                 Predicate<@NotNull ItemStack> validator,
                                 BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert,
                                 BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract) {
        this(index, attachedGetter, attachedSetter, attachedCreator, validator, canInsert, canExtract,
                () -> Item.ABSOLUTE_MAX_STACK_SIZE, () -> true);
    }

    public AttachedItemContainer(int index,
                                 Supplier<@NotNull AttachedItems> attachedGetter,
                                 Consumer<@NotNull AttachedItems> attachedSetter,
                                 Supplier<@NotNull AttachedItems> attachedCreator,
                                 Predicate<@NotNull ItemStack> validator,
                                 BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert,
                                 BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract,
                                 IntSupplier limit,
                                 BooleanSupplier obeyStackLimit) {
        super(index, attachedGetter, attachedSetter, attachedCreator);
        this.validator = validator;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.limit = limit;
        this.obeyStackLimit = obeyStackLimit;
    }

    public boolean canInsert(@NotNull ItemStack stack, @NotNull AutomationType automationType) {
        return canInsert.test(stack, automationType);
    }

    public boolean canExtract(@NotNull ItemStack stack, @NotNull AutomationType automationType) {
        return canExtract.test(stack, automationType);
    }

    @Override
    public boolean isItemValid(@NotNull ItemStack stack) {
        return validator.test(stack);
    }

    @Override
    public int getLimit(@NotNull ItemStack stack) {
        return obeyStackLimit.getAsBoolean() && !stack.isEmpty() ? Math.min(limit.getAsInt(), stack.getMaxStackSize()) : limit.getAsInt();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return getContents(getAttached());
    }

    @Override
    public void setItem(@NotNull ItemStack stack) {
        setContents(getAttached(), stack);
    }

    @Override
    public @NotNull ItemStack insert(@NotNull ItemStack stack, @NotNull Action action, @NotNull AutomationType automationType) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        AttachedItems attachedItems = getAttached();
        ItemStack current = getContents(attachedItems);
        int needed = getLimit(stack) - current.getCount();
        if (needed <= 0 || !isItemValid(stack) || !canInsert(stack, automationType)) {
            return stack;
        } else if (current.isEmpty() || ItemStack.isSameItemSameComponents(current, stack)) {
            int toAdd = Math.min(stack.getCount(), needed);
            if (action.execute()) {
                setContents(attachedItems, stack.copyWithCount(current.getCount() + toAdd));
            }
            return stack.copyWithCount(stack.getCount() - toAdd);
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack extract(int amount, @NotNull Action action, @NotNull AutomationType automationType) {
        if (amount < 1) {
            return ItemStack.EMPTY;
        }
        AttachedItems attachedItems = getAttached();
        ItemStack current = getContents(attachedItems);
        if (current.isEmpty() || !canExtract(current, automationType)) {
            return ItemStack.EMPTY;
        }
        int currentAmount = Math.min(current.getCount(), current.getMaxStackSize());
        if (currentAmount < amount) {
            amount = currentAmount;
        }
        ItemStack toReturn = current.copyWithCount(amount);
        if (action.execute()) {
            setContents(attachedItems, current.copyWithCount(current.getCount() - amount));
        }
        return toReturn;
    }
}
