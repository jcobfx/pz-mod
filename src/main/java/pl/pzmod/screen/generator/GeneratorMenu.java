package pl.pzmod.screen.generator;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;
import pl.pzmod.registries.PZBlocks;
import pl.pzmod.registries.PZMenuTypes;

public class GeneratorMenu extends AbstractContainerMenu {
    public final GeneratorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public GeneratorMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    public GeneratorMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data) {
        super(PZMenuTypes.GENERATOR.get(), containerId);
        checkContainerSize(inventory, 1);
        this.blockEntity = ((GeneratorBlockEntity) blockEntity);
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 26, 30));

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

    public int getEnergyStored() { return this.data.get(0); }
    public int getMaxEnergy() { return this.data.get(1); }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, PZBlocks.GENERATOR.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
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