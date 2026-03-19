package pl.pzmod.capabilities.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;

import java.util.List;
import java.util.Optional;

public interface IPZItemHandler extends ISidedItemHandler {
    @NotNull
    List<IItemContainer> getItemContainers(@Nullable Direction side);

    default Optional<IItemContainer> getItemContainer(int slot, @Nullable Direction side) {
        var containers = getItemContainers(side);
        return Optional.ofNullable(slot >= 0 && slot < containers.size() ? containers.get(slot) : null);
    }

    default boolean hasItemContainers() {
        return true;
    }

    @Override
    default void setStackInSlot(int slot, @NotNull ItemStack stack, @Nullable Direction side) {
        getItemContainer(slot, side).ifPresent(c -> c.setItem(stack));
    }

    @Override
    default int getSlots(@Nullable Direction side) {
        return getItemContainers(side).size();
    }

    @Override
    default @NotNull ItemStack getStackInSlot(int slot, @Nullable Direction side) {
        return getItemContainer(slot, side).map(IItemContainer::getItem).orElse(ItemStack.EMPTY);
    }

    @Override
    default @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, @NotNull Action action, @Nullable Direction side) {
        return getItemContainer(slot, side).map(c -> c.insert(stack, action, AutomationType.handler(side))).orElse(stack);
    }

    @Override
    default @NotNull ItemStack extractItem(int slot, int amount, @NotNull Action action, @Nullable Direction side) {
        return getItemContainer(slot, side).map(c -> c.extract(amount, action, AutomationType.handler(side))).orElse(ItemStack.EMPTY);
    }

    @Override
    default int getSlotLimit(int slot, @Nullable Direction side) {
        return getItemContainer(slot, side).map(c -> c.getLimit(ItemStack.EMPTY)).orElse(0);
    }

    @Override
    default boolean isItemValid(int slot, @NotNull ItemStack stack, @Nullable Direction side) {
        return getItemContainer(slot, side).map(c -> c.isItemValid(stack)).orElse(false);
    }
}
