package pl.pzmod.capabilities.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;

public interface IItemContainer {
    @NotNull ItemStack getItem();

    void setItem(@NotNull ItemStack stack);

    boolean isItemValid(@NotNull ItemStack stack);

    int getLimit(@NotNull ItemStack stack);

    default int getCount() {
        return getItem().getCount();
    }

    default boolean isEmpty() {
        return getItem().isEmpty();
    }

    /**
     *
     * @param stack           items to insert
     * @param action         action to perform
     * @param automationType automation type
     * @return items that could not be inserted
     */
    default @NotNull ItemStack insert(@NotNull ItemStack stack, @NotNull Action action, @NotNull AutomationType automationType) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int needed = getLimit(stack) - getCount();
        if (needed <= 0 || !isItemValid(stack)) {
            return stack;
        }
        var current = getItem();
        boolean empty = isEmpty();
        boolean sameType = !empty && ItemStack.isSameItemSameComponents(current, stack);
        if (isEmpty() || sameType) {
            int toAdd = Math.min(stack.getCount(), needed);
            if (action.execute()) {
                if (sameType) {
                    setItem(current.copyWithCount(current.getCount() + toAdd));
                } else {
                    setItem(stack.copyWithCount(toAdd));
                }
            }
            return stack.copyWithCount(stack.getCount() - toAdd);
        }
        return stack;
    }

    /**
     *
     * @param amount         amount to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return extracted items
     */
    default @NotNull ItemStack extract(int amount, @NotNull Action action, @NotNull AutomationType automationType) {
        if (isEmpty() || amount < 1) {
            return ItemStack.EMPTY;
        }
        ItemStack current = getItem();
        int currentAmount = Math.min(current.getCount(), current.getMaxStackSize());
        if (currentAmount < amount) {
            amount = currentAmount;
        }
        ItemStack toReturn = current.copyWithCount(amount);
        if (action.execute()) {
            setItem(current.copyWithCount(current.getCount() - amount));
        }
        return toReturn;
    }
}
