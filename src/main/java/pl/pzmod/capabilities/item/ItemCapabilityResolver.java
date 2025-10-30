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

public class ItemCapabilityResolver<CONTEXT> extends CapabilityResolver<CONTEXT, AttachedItems> implements IItemHandler {
    private final int slotCount;
    private final int slotLimit;

    protected ItemCapabilityResolver(MutableDataComponentHolder parent,
                                     CONTEXT context,
                                     int slotCount,
                                     int slotLimit,
                                     Predicate<CONTEXT> canInsert,
                                     Predicate<CONTEXT> canExtract) {
        super(parent, PZDataComponents.ATTACHED_ITEMS, context, canInsert, canExtract);
        this.slotCount = slotCount;
        this.slotLimit = slotLimit;
    }

    @Override
    public int getSlots() {
        return this.slotCount;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        AttachedItems contents = this.getContents();
        return this.getStackFromContents(contents, slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack toInsert, boolean simulate) {
        this.validateSlotIndex(slot);
        if (toInsert.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!canInsert() || !this.isItemValid(slot, toInsert)) {
            return toInsert;
        } else {
            AttachedItems contents = this.getContents();
            ItemStack existing = this.getStackFromContents(contents, slot);
            int insertLimit = Math.min(this.getSlotLimit(slot), toInsert.getMaxStackSize());
            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(toInsert, existing)) {
                    return toInsert;
                }
                insertLimit -= existing.getCount();
            }
            if (insertLimit <= 0) {
                return toInsert;
            } else {
                int inserted = Math.min(insertLimit, toInsert.getCount());
                if (!simulate) {
                    this.updateContents(contents, toInsert.copyWithCount(existing.getCount() + inserted), slot);
                }
                return toInsert.copyWithCount(toInsert.getCount() - inserted);
            }
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        this.validateSlotIndex(slot);
        if (!canExtract() || amount == 0) {
            return ItemStack.EMPTY;
        } else {
            AttachedItems contents = getContents();
            ItemStack existing = this.getStackFromContents(contents, slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getCount());
                if (!simulate) {
                    this.updateContents(contents, existing.copyWithCount(existing.getCount() - toExtract), slot);
                }
                return existing.copyWithCount(toExtract);
            }
        }
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
        this.validateSlotIndex(slot);
        NonNullList<ItemStack> list = NonNullList.withSize(Math.max(contents.items().size(), this.getSlots()), ItemStack.EMPTY);
        contents.copyInto(list);
        ItemStack oldStack = list.get(slot);
        list.set(slot, stack);
        setAttached(new AttachedItems(list));
        onContentsChanged(slot, oldStack, stack);
    }

    protected final void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.getSlots()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.getSlots() + ")");
        }
    }
}
