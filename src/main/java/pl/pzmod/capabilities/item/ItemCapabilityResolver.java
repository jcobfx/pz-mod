package pl.pzmod.capabilities.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.CapabilityResolver;
import pl.pzmod.data.containers.AttachedItems;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class ItemCapabilityResolver<HOLDER extends MutableDataComponentHolder, CONTEXT>
        extends CapabilityResolver<HOLDER, CONTEXT, AttachedItems> implements IItemHandler {
    private final int slotCount;
    private final int slotLimit;

    protected ItemCapabilityResolver(HOLDER parent,
                                     CONTEXT context,
                                     Predicate<CONTEXT> canInsert,
                                     Predicate<CONTEXT> canExtract,
                                     int slots,
                                     int limit) {
        super(parent, PZDataComponents.ATTACHED_ITEMS, context, canInsert, canExtract);
        this.slotCount = slots;
        this.slotLimit = limit;
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

    protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
    }

    protected AttachedItems getContents() {
        return getAttached(AttachedItems.EMPTY);
    }

    protected ItemStack getStackFromContents(@NotNull AttachedItems contents, int slot) {
        this.validateSlotIndex(slot);
        return contents.getSlots() <= slot ? ItemStack.EMPTY : contents.getStackInSlot(slot);
    }

    protected void updateContents(@NotNull AttachedItems contents, ItemStack stack, int slot) {
        validateSlotIndex(slot);
        NonNullList<ItemStack> list = NonNullList.withSize(Math.max(contents.getSlots(), getSlots()), ItemStack.EMPTY);
        contents.copyInto(list);
        ItemStack oldStack = list.get(slot);
        list.set(slot, stack);
        setAttached(new AttachedItems(list));
        onContentsChanged(slot, oldStack, stack);
    }

    protected final void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= getSlots()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
        }
    }
}
