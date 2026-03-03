package pl.pzmod.capabilities.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.CapabilityResolver;
import pl.pzmod.capabilities.proxy.Proxy;
import pl.pzmod.data.containers.AttachedItems;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemCapabilityResolver<P, T, C> extends CapabilityResolver<Proxy<P>, T, C> implements IItemHandlerModifiable {
    private final int slotCount;
    private final int slotLimit;

    protected ItemCapabilityResolver(Proxy<P> dataHolder,
                                     Supplier<T> dataType,
                                     C context,
                                     Predicate<C> canInsert,
                                     Predicate<C> canExtract) {
        super(dataHolder, dataType, context, canInsert, canExtract);
        this.slotCount = dataHolder.getSlotCount();
        this.slotLimit = dataHolder.getSlotLimit();
    }

    @Override
    public int getSlots() {
        return slotCount;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        AttachedItems contents = getContents();
        return getStackFromContents(contents, slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack toInsert, boolean simulate) {
        validateSlotIndex(slot);
        if (toInsert.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (!canInsert() || !isItemValid(slot, toInsert)) {
            return toInsert;
        }

        AttachedItems contents = getContents();
        ItemStack existing = getStackFromContents(contents, slot);
        int insertLimit = getSlotLimit(slot);
        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(toInsert, existing)) {
                return toInsert;
            }
            insertLimit -= existing.getCount();
        }
        if (insertLimit <= 0) {
            return toInsert;
        }

        int inserted = Math.min(insertLimit, toInsert.getCount());
        if (!simulate) {
            updateContents(contents, toInsert.copyWithCount(existing.getCount() + inserted), slot);
        }
        return toInsert.copyWithCount(toInsert.getCount() - inserted);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        validateSlotIndex(slot);
        if (!canExtract() || amount == 0) {
            return ItemStack.EMPTY;
        }

        AttachedItems contents = getContents();
        ItemStack existing = getStackFromContents(contents, slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, Math.min(existing.getCount(), existing.getMaxStackSize()));
        if (!simulate) {
            updateContents(contents, existing.copyWithCount(existing.getCount() - toExtract), slot);
        }
        return existing.copyWithCount(toExtract);
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.canFitInsideContainerItems();
    }

    @Override
    public void setStackInSlot(int i, @NotNull ItemStack itemStack) {
        validateSlotIndex(i);
        if (!canInsert() && !itemStack.isEmpty()) {
            throw new IllegalArgumentException("Cannot insert into slot " + i + " - insertion is not allowed");
        }
        if (!isItemValid(i, itemStack)) {
            throw new IllegalArgumentException("Cannot insert stack " + itemStack + " into slot " + i + " - stack is not valid for this slot");
        }

        AttachedItems contents = getContents();
        ItemStack oldStack = getStackFromContents(contents, i);
        updateContents(contents, itemStack, i);
        onContentsChanged(i, oldStack, itemStack);
    }

    protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
        // Do nothing by default, can be overridden to react to changes in the item contents
    }

    private AttachedItems getContents() {
        return getData(AttachedItems.EMPTY);
    }

    private ItemStack getStackFromContents(@NotNull AttachedItems contents, int slot) {
        this.validateSlotIndex(slot);
        return contents.getSlots() <= slot ? ItemStack.EMPTY : contents.getStackInSlot(slot);
    }

    private void updateContents(@NotNull AttachedItems contents, ItemStack stack, int slot) {
        validateSlotIndex(slot);
        NonNullList<ItemStack> list = NonNullList.withSize(Math.max(contents.getSlots(), getSlots()), ItemStack.EMPTY);
        contents.copyInto(list);
        ItemStack oldStack = list.get(slot);
        list.set(slot, stack);
        setData(new AttachedItems(list));
        onContentsChanged(slot, oldStack, stack);
    }

    private void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
        }
    }
}
