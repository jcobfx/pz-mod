package pl.pzmod.data.containers.items;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;

public interface ISidedItemHandler extends IItemHandlerModifiable {
    int getSlots(@Nullable Direction side);

    @NotNull
    ItemStack getStackInSlot(int slot, @Nullable Direction side);

    void setStackInSlot(int slot, @NotNull ItemStack stack, @Nullable Direction side);

    int getSlotLimit(int slot, @Nullable Direction side);

    boolean isItemValid(int slot, @NotNull ItemStack stack, @Nullable Direction side);

    /**
     *
     * @param slot   index of slot
     * @param stack  items to insert
     * @param action action to perform
     * @param side   side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return items that could not be inserted
     */
    @NotNull
    ItemStack insertItem(int slot, @NotNull ItemStack stack, @NotNull Action action, @Nullable Direction side);

    /**
     *
     * @param slot   index of slot
     * @param amount amount to extract
     * @param action action to perform
     * @param side   side from which the insertion is performed, or null if it is performed internally (e.g. by a machine's GUI)
     * @return extracted items
     */
    @NotNull
    ItemStack extractItem(int slot, int amount, @NotNull Action action, @Nullable Direction side);

    default @Nullable Direction getItemHandlerSideFor() {
        return null;
    }

    @Override
    default int getSlots() {
        return getSlots(getItemHandlerSideFor());
    }

    @Override
    default @NotNull ItemStack getStackInSlot(int slot) {
        return getStackInSlot(slot, getItemHandlerSideFor());
    }

    @Override
    default void setStackInSlot(int slot, @NotNull ItemStack stack) {
        setStackInSlot(slot, stack, getItemHandlerSideFor());
    }

    @Override
    default int getSlotLimit(int slot) {
        return getSlotLimit(slot, getItemHandlerSideFor());
    }

    @Override
    default boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return isItemValid(slot, stack, getItemHandlerSideFor());
    }

    /**
     *
     * @param slot     index of slot
     * @param stack    items to insert
     * @param simulate if true, the insertion is only simulated and no changes should be made to the container
     * @return items that could not be inserted
     */
    @Override
    default @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return insertItem(slot, stack, Action.get(!simulate), getItemHandlerSideFor());
    }

    /**
     *
     * @param slot     index of slot
     * @param amount   amount to extract
     * @param simulate if true, the insertion is only simulated and no changes should be made to the container
     * @return extracted items
     */
    @Override
    default @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extractItem(slot, amount, Action.get(!simulate), getItemHandlerSideFor());
    }
}
