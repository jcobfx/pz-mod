package pl.pzmod.screen.generator;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.registries.PZBlocks;
import pl.pzmod.registries.PZMenuTypes;

import java.util.Objects;

public class GeneratorMenu extends AbstractContainerMenu {
    private final IEnergyStorage energyStorage;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public GeneratorMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId,
                playerInventory,
                new ItemStackHandler(1),
                Objects.requireNonNull(playerInventory.player.level()
                        .getCapability(Capabilities.EnergyStorage.BLOCK, extraData.readBlockPos(), null)),
                new SimpleContainerData(4),
                ContainerLevelAccess.NULL);
    }

    public GeneratorMenu(int containerId,
                         Inventory playerInventory,
                         IItemHandler inventory,
                         IEnergyStorage energyStorage,
                         ContainerData data,
                         ContainerLevelAccess access) {
        super(PZMenuTypes.GENERATOR.get(), containerId);
        checkContainerSize(playerInventory, 1);
        checkContainerDataCount(data, 2);
        this.energyStorage = energyStorage;
        this.data = data;
        this.access = access;

        this.addSlot(new SlotItemHandler(inventory, 0, 26, 30)); // index 0 - fuel slot
        addPlayerInventory(playerInventory); // index 1 - 27
        addPlayerHotbar(playerInventory); // index 28 - 36
        addDataSlots(data);
    }

    int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    int getBurnTime() {
        return data.get(0);
    }

    int getBurnTimeTotal() {
        return data.get(1);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, PZBlocks.GENERATOR.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = slots.get(quickMovedSlotIndex);
        if (!quickMovedSlot.hasItem()) {
            return quickMovedStack;
        }

        ItemStack rawStack = quickMovedSlot.getItem();
        quickMovedStack = rawStack.copy();
        if (quickMovedSlotIndex == 0) {
            if (!moveItemStackTo(rawStack, 1, 37, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(rawStack, 0, 1, false)) {
            if (quickMovedSlotIndex >= 1 && quickMovedSlotIndex < 27) {
                if (!moveItemStackTo(rawStack, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(rawStack, 1, 28, false)) {
                return ItemStack.EMPTY;
            }
        }
        if (rawStack.isEmpty()) {
            quickMovedSlot.set(ItemStack.EMPTY);
        } else {
            quickMovedSlot.setChanged();
        }
        return quickMovedStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}