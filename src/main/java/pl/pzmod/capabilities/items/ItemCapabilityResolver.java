package pl.pzmod.capabilities.items;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.items.ISidedItemHandler;
import pl.pzmod.items.PZItem;

import java.util.Objects;

public class ItemCapabilityResolver implements ISidedItemHandler {
    private final ISidedItemHandler handler;
    private final @Nullable Direction side;

    public static ISidedItemHandler forBlockEntity(@NotNull PZBlockEntity blockEntity, @Nullable Direction side) {
        return new ItemCapabilityResolver(Objects.requireNonNull(blockEntity.getItemHandler(blockEntity)), side);
    }

    public static ISidedItemHandler forItem(@NotNull ItemStack stack, @NotNull PZItem item) {
        return new ItemCapabilityResolver(Objects.requireNonNull(item.getItemHandler(stack)), null);
    }

    private ItemCapabilityResolver(@NotNull ISidedItemHandler handler, @Nullable Direction side) {
        this.handler = handler;
        this.side = side;
    }

    @Override
    public @Nullable Direction getDefaultSide() {
        return side;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack, @Nullable Direction side) {
        handler.setStackInSlot(slot, stack, side);
    }

    @Override
    public int getSlots(@Nullable Direction side) {
        return handler.getSlots(side);
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot, @Nullable Direction side) {
        return handler.getStackInSlot(slot, side);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, @Nullable Direction side, @NotNull Action action) {
        return handler.insertItem(slot, stack, side, action);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, @Nullable Direction side, @NotNull Action action) {
        return handler.extractItem(slot, amount, side, action);
    }

    @Override
    public int getSlotLimit(int slot, @Nullable Direction side) {
        return handler.getSlotLimit(slot, side);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack, @Nullable Direction side) {
        return handler.isItemValid(slot, stack, side);
    }
}
