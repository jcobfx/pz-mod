package pl.pzmod.screen.generator;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.registries.PZBlocks;
import pl.pzmod.registries.PZMenuTypes;

public class GeneratorMenu extends AbstractContainerMenu {
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public GeneratorMenu(int containerId, Inventory playerInventory) {
        this(containerId,
                playerInventory,
                new ItemStackHandler(1),
                new SimpleContainerData(4),
                ContainerLevelAccess.NULL);
    }

    public GeneratorMenu(int containerId, Inventory playerInventory, IItemHandler inventory, ContainerData data, ContainerLevelAccess access) {
        super(PZMenuTypes.GENERATOR.get(), containerId);
        int i = inventory.getSlots();
        if (i < 1) {
            throw new IllegalArgumentException("Container size " + i + " is smaller than expected " + 1);
        }
        checkContainerSize(playerInventory, 1);
        this.data = data;
        this.access = access;

        this.addSlot(new SlotItemHandler(inventory, 0, 26, 30));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(data);
    }

    public int getEnergyScaled() {
        int energy = this.data.get(0);
        int maxEnergy = this.data.get(1);
        int totalBarHeight = 46;

        if (maxEnergy <= 0 || energy <= 0) return 0;
        return (int) ((long) energy * totalBarHeight / maxEnergy);
    }

    public int getBurnProgress() {
        int burn = this.data.get(2);
        int total = this.data.get(3);
        int totalArrowWidth = 25;

        if (total <= 0 || burn <= 0) return 0;
        return (burn * totalArrowWidth / total);
    }

    public int getEnergyStored() {
        return this.data.get(0);
    }

    public int getMaxEnergy() {
        return this.data.get(1);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, PZBlocks.GENERATOR.get());
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < 36) {
            if (!moveItemStackTo(sourceStack, 36, 37, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < 37) {
            if (!moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        return copyOfSourceStack;
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