package pl.pzmod.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;
import pl.pzmod.registries.PZBlocks;
import pl.pzmod.registries.PZMenuTypes;

public class GeneratorMenu extends AbstractContainerMenu {
    private final Level level;
    private final GeneratorBlockEntity blockEntity;
    private final ContainerData data;

    public GeneratorMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId,
                playerInventory,
                playerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public GeneratorMenu(int containerId,
                         Inventory playerInventory,
                         BlockEntity blockEntity) {
        super(PZMenuTypes.GENERATOR.get(), containerId);
        checkContainerSize(playerInventory, 1);
        this.level = playerInventory.player.level();
        this.blockEntity = (GeneratorBlockEntity) blockEntity;
        this.data = this.blockEntity.getData();
        checkContainerDataCount(data, 2);

        this.addSlot(new SlotItemHandler(new InvWrapper(this.blockEntity), 0, 26, 30)); // index 0 - fuel slot
        addPlayerInventory(playerInventory); // index 1 - 27
        addPlayerHotbar(playerInventory); // index 28 - 36
        addDataSlots(data);
    }

    int getEnergyStored() {
        return blockEntity.getEnergyStored();
    }

    int getMaxEnergyStored() {
        return blockEntity.getMaxEnergyStored();
    }

    int getBurnTime() {
        return data.get(0);
    }

    int getBurnTimeTotal() {
        return data.get(1);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, PZBlocks.GENERATOR.get());
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
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, Inventory.getSelectionSize() + x + y * 9, 8 + x * 18, 84 + y * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int x = 0; x < Inventory.getSelectionSize(); ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }
}