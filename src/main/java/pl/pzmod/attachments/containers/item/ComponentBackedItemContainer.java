package pl.pzmod.attachments.containers.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;
import pl.pzmod.attachments.containers.ComponentBackedContainer;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.item.IItemContainer;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class ComponentBackedItemContainer extends ComponentBackedContainer<ItemStack, AttachedItems> implements IItemContainer {
    private final BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert;
    private final BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract;
    private final Predicate<@NotNull ItemStack> validator;
    private final boolean obeyStackLimit;
    private final int limit;

    public ComponentBackedItemContainer(@NotNull ItemStack attachedTo,
                                        int containerIndex,
                                        BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert,
                                        BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract,
                                        Predicate<@NotNull ItemStack> validator) {
        this(attachedTo, containerIndex, canInsert, canExtract, validator, true, Item.ABSOLUTE_MAX_STACK_SIZE);
    }

    public ComponentBackedItemContainer(@NotNull ItemStack attachedTo,
                                        int containerIndex,
                                        BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert,
                                        BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract,
                                        Predicate<@NotNull ItemStack> validator,
                                        boolean obeyStackLimit,
                                        int limit) {
        super(attachedTo, containerIndex);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.validator = validator;
        this.obeyStackLimit = obeyStackLimit;
        this.limit = limit;
    }

    @Override
    protected @NotNull ItemStack copy(ItemStack toCopy) {
        return toCopy.copy();
    }

    @Override
    protected @NotNull ContainerType<?, AttachedItems, ?> getContainerType() {
        return ContainerType.ITEMS;
    }

    @Override
    public int getLimit(@NotNull ItemStack stack) {
        return obeyStackLimit && !stack.isEmpty() ? Math.min(limit, stack.getMaxStackSize()) : limit;
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
    public boolean isItemValid(@NotNull ItemStack stack) {
        return validator.test(stack);
    }

    @Override
    public @NotNull ItemStack insert(@NotNull ItemStack stack, @NotNull Action action, @NotNull AutomationType automationType) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        AttachedItems attachedItems = getAttached();
        ItemStack current = getContents(attachedItems);
        int needed = getLimit(stack) - current.getCount();
        if (needed <= 0 || !isItemValid(stack) || !canInsert.test(stack, automationType)) {
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
        if (current.isEmpty() || !canExtract.test(current, automationType)) {
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
