package pl.pzmod.capabilities.proxy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.items.ISidedItemHandler;

public class ItemHandlerProxy implements IItemHandlerModifiable {
    private final ISidedItemHandler sidedItemHandler;
    private final @Nullable Direction side;

    public ItemHandlerProxy(@NotNull ISidedItemHandler sidedItemHandler, @Nullable Direction side) {
        this.sidedItemHandler = sidedItemHandler;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return sidedItemHandler.getSlots(side);
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return sidedItemHandler.getStackInSlot(slot, side);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return sidedItemHandler.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return sidedItemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return sidedItemHandler.getSlotLimit(slot, side);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return sidedItemHandler.isItemValid(slot, stack, side);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        sidedItemHandler.setStackInSlot(slot, stack, side);
    }
}
