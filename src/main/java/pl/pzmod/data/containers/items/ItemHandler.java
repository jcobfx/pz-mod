package pl.pzmod.data.containers.items;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.MutableContainerHandler;

import java.util.function.Supplier;

public class ItemHandler extends MutableContainerHandler<IItemContainer> implements ISidedItemHandler {
    public ItemHandler() {
        this(null);
    }

    public ItemHandler(@Nullable Supplier<Direction> facing) {
        super(facing);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack, @Nullable Direction side) {
        getContainers(side).get(slot).setItem(stack);
    }

    @Override
    public int getSlots(@Nullable Direction side) {
        return getContainers(side).size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot, @Nullable Direction side) {
        return getContainers(side).get(slot).getItem();
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, @Nullable Direction side, @NotNull Action action) {
        return getContainers(side).get(slot).insert(stack, action, AutomationType.handler(side));
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, @Nullable Direction side, @NotNull Action action) {
        return getContainers(side).get(slot).extract(amount, action, AutomationType.handler(side));
    }

    @Override
    public int getSlotLimit(int slot, @Nullable Direction side) {
        return getContainers(side).get(slot).getCapacity();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack, @Nullable Direction side) {
        return getContainers(side).get(slot).isItemValid(stack);
    }
}
