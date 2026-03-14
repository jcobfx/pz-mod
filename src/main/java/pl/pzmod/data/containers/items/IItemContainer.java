package pl.pzmod.data.containers.items;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;

public interface IItemContainer {
    int getCapacity();

    @NotNull ItemStack getItem();

    void setItem(@NotNull ItemStack stack);

    boolean isItemValid(@NotNull ItemStack stack);

    boolean canInsert(@NotNull AutomationType automationType);

    boolean canExtract(@NotNull AutomationType automationType);

    /**
     *
     * @param item           items to insert
     * @param action         action to perform
     * @param automationType automation type
     * @return items that could not be inserted
     */
    default @NotNull ItemStack insert(@NotNull ItemStack item, @NotNull Action action, @NotNull AutomationType automationType) {
        var stored = getItem();
        if (item.isEmpty() || !canInsert(automationType) || !isItemValid(item)
                || (!stored.isEmpty() && !ItemStack.isSameItemSameComponents(stored, item))) {
            return item;
        }
        int storedAmount = stored.getCount();
        int toInsert = item.getCount();
        int inserted = Math.min(toInsert, getCapacity() - storedAmount);
        if (inserted > 0 && action.execute()) {
            setItem(item.copyWithCount(storedAmount + inserted));
        }
        return item.copyWithCount(toInsert - inserted);
    }

    /**
     *
     * @param amount         amount to extract
     * @param action         action to perform
     * @param automationType automation type
     * @return extracted items
     */
    default @NotNull ItemStack extract(int amount, @NotNull Action action, @NotNull AutomationType automationType) {
        if (amount <= 0 || !canExtract(automationType)) {
            return ItemStack.EMPTY;
        }
        var stored = getItem();
        int storedAmount = stored.getCount();
        int extracted = Math.min(amount, storedAmount);
        if (extracted > 0 && action.execute()) {
            setItem(stored.copyWithCount(storedAmount - extracted));
        }
        return stored.copyWithCount(extracted);
    }
}
