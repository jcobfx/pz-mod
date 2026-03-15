package pl.pzmod.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class PZContainerMenu extends AbstractContainerMenu {
    private static @NotNull ItemStack insertItem(List<Slot> slots, @NotNull ItemStack stack, boolean ignoreEmpty) {
        if (stack.isEmpty()) {
            return stack;
        }
        for (Slot slot : slots) {
            if (!ignoreEmpty || slot.hasItem()) {
                stack = insertItem(slot, stack);
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
        return stack;
    }

    private static @NotNull ItemStack insertItem(@NotNull Slot slot, @NotNull ItemStack stack) {
        if (stack.isEmpty() || !slot.mayPlace(stack)) {
            return stack;
        }
        ItemStack current = slot.getItem();
        int needed = slot.getMaxStackSize(stack) - current.getCount();
        if (needed <= 0) {
            return stack;
        }
        if (current.isEmpty() || ItemStack.isSameItemSameComponents(current, stack)) {
            int toAdd = Math.min(stack.getCount(), needed);
            slot.set(stack.copyWithCount(current.getCount() + toAdd));
            return stack.copyWithCount(stack.getCount() - toAdd);
        }
        return stack;
    }

    private final Inventory playerInventory;
    private final Map<SlotType, List<Slot>> typedSlots;

    protected PZContainerMenu(Supplier<MenuType<PZContainerMenu>> menuType, int containerId, Inventory playerInventory) {
        super(menuType.get(), containerId);
        this.playerInventory = playerInventory;
        this.typedSlots = new EnumMap<>(SlotType.class);
    }

    protected int getInventoryYOffset() {
        return 84;
    }

    protected int getInventoryXOffset() {
        return 8;
    }

    protected @NotNull Slot addSlot(@NotNull Slot slot, @NotNull SlotType type) {
        slot = new TypedSlot(slot, type);
        super.addSlot(slot);
        typedSlots.computeIfAbsent(type, t -> new ArrayList<>()).add(slot);
        return slot;
    }

    @Override
    protected @NotNull Slot addSlot(@NotNull Slot slot) {
        return addSlot(slot, SlotType.INVENTORY_CONTAINER);
    }

    protected void initSlots() {
        addSlots();
        addInventorySlots(playerInventory);
    }

    protected void addSlots() {
    }

    private void addInventorySlots(@NotNull Inventory playerInventory) {
        int yOffset = getInventoryYOffset();
        int xOffset = getInventoryXOffset();
        for (int slotY = 0; slotY < 3; slotY++) {
            for (int slotX = 0; slotX < 9; slotX++) {
                addSlot(new Slot(playerInventory, Inventory.getSelectionSize() + slotX + slotY * 9,
                        xOffset + slotX * 18, yOffset + slotY * 18), SlotType.MAIN_INVENTORY);
            }
        }
        yOffset += 58;
        for (int slotX = 0; slotX < Inventory.getSelectionSize(); slotX++) {
            addSlot(new Slot(playerInventory, slotX, xOffset + slotX * 18, yOffset), SlotType.HOTBAR);
        }
    }

    private @NotNull ItemStack insertItem(@NotNull SlotType type, @NotNull ItemStack stack, boolean ignoreEmpty) {
        return insertItem(typedSlots.get(type), stack, ignoreEmpty);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        TypedSlot currentSlot = (TypedSlot) slots.get(slot);
        if (!currentSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack slotStack = currentSlot.getItem();
        ItemStack stackToInsert = slotStack;
        SlotType currentSlotType = currentSlot.type;
        if (currentSlotType == SlotType.INVENTORY_CONTAINER) {
            if (slotStack.getCount() > slotStack.getMaxStackSize()) {
                stackToInsert = slotStack = slotStack.copyWithCount(slotStack.getMaxStackSize());
            }
            stackToInsert = insertItem(SlotType.HOTBAR, stackToInsert, true);
            stackToInsert = insertItem(SlotType.MAIN_INVENTORY, stackToInsert, true);
            stackToInsert = insertItem(SlotType.HOTBAR, stackToInsert, false);
            stackToInsert = insertItem(SlotType.MAIN_INVENTORY, stackToInsert, false);
        } else {
            stackToInsert = insertItem(typedSlots.get(SlotType.INVENTORY_CONTAINER), slotStack, true);
            if (slotStack.getCount() == stackToInsert.getCount()) {
                if (currentSlotType == SlotType.MAIN_INVENTORY) {
                    stackToInsert = insertItem(SlotType.HOTBAR, stackToInsert, true);
                    stackToInsert = insertItem(SlotType.HOTBAR, stackToInsert, false);
                } else if (currentSlotType == SlotType.HOTBAR) {
                    stackToInsert = insertItem(SlotType.MAIN_INVENTORY, stackToInsert, true);
                    stackToInsert = insertItem(SlotType.MAIN_INVENTORY, stackToInsert, false);
                }
            }
        }
        if (stackToInsert.getCount() == slotStack.getCount()) {
            return ItemStack.EMPTY;
        }
        return transferSuccess(currentSlot, player, stackToInsert, slotStack);
    }

    @NotNull
    protected ItemStack transferSuccess(@NotNull Slot currentSlot,
                                        @NotNull Player player,
                                        @NotNull ItemStack slotStack,
                                        @NotNull ItemStack stackToInsert) {
        int difference = slotStack.getCount() - stackToInsert.getCount();
        ItemStack newStack = currentSlot.remove(difference);
        currentSlot.onTake(player, newStack);
        return newStack;
    }

    public enum SlotType {
        INVENTORY_CONTAINER,
        MAIN_INVENTORY,
        HOTBAR
    }

    private static class TypedSlot extends Slot {
        private final SlotType type;

        public TypedSlot(Slot slot, SlotType type) {
            super(slot.container, slot.index, slot.x, slot.y);
            this.type = type;
        }
    }
}
