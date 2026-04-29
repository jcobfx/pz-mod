package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.items.AttachedItems;
import pl.pzmod.menus.GeneratorMenu;
import pl.pzmod.registries.PZAttachments;
import pl.pzmod.registries.PZBlockEntities;

// TODO: Add block capability caching
public class GeneratorBlockEntity extends BlockEntity implements IEnergyStorage, IItemHandlerModifiable, MenuProvider {
    private static final int ENERGY_CAPACITY = 100_000;
    private static final int ENERGY_MAX_TRANSFER = 1000;
    private static final int ENERGY_PER_TICK = 20;

    public static void tickServer(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        boolean changed = false;
        if (blockEntity.burnTime > 0) {
            blockEntity.burnTime--;
            blockEntity.receiveEnergy(ENERGY_PER_TICK, false);
            changed = true;
        }

        if (blockEntity.burnTime <= 0 && blockEntity.getEnergyStored() < blockEntity.getMaxEnergyStored()) {
            ItemStack fuelStack = blockEntity.extractItem(0, 1, true);
            if (!fuelStack.isEmpty()) {
                int burnTicks = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTicks > 0) {
                    blockEntity.burnTime = burnTicks;
                    blockEntity.burnTimeTotal = burnTicks;
                    blockEntity.extractItem(0, 1, false);
                    changed = true;
                }
            }
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }

    private final ContainerData data;

    private int burnTime = 0;
    private int burnTimeTotal = 0;

    public GeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(PZBlockEntities.GENERATOR.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GeneratorBlockEntity.this.burnTime;
                    case 1 -> GeneratorBlockEntity.this.burnTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GeneratorBlockEntity.this.burnTime = value;
                    case 1 -> GeneratorBlockEntity.this.burnTimeTotal = value;
                    default -> {
                        // Ignore invalid index
                    }
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("burnTime", burnTime);
        tag.putInt("burnTimeTotal", burnTimeTotal);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        burnTime = tag.getInt("burnTime");
        burnTimeTotal = tag.getInt("burnTimeTotal");
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.pz_mod.generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new GeneratorMenu(i, inventory, this);
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate) {
        int current = getEnergyStored();
        int space = getMaxEnergyStored() - current;
        int toReceive = Math.min(space, Math.min(energy, ENERGY_MAX_TRANSFER));
        if (!simulate) {
            setData(PZAttachments.ENERGY_CONTAINER, current + toReceive);
        }
        return toReceive;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate) {
        int current = getEnergyStored();
        int toExtract = Math.min(current, Math.min(energy, ENERGY_MAX_TRANSFER));
        if (!simulate) {
            setData(PZAttachments.ENERGY_CONTAINER, current - toExtract);
        }
        return toExtract;
    }

    @Override
    public int getEnergyStored() {
        return getData(PZAttachments.ENERGY_CONTAINER);
    }

    @Override
    public int getMaxEnergyStored() {
        return ENERGY_CAPACITY;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    private AttachedItems getAttachedItems() {
        return getExistingData(PZAttachments.ITEM_CONTAINER).orElse(AttachedItems.withSize(getSlots()));
    }

    private void updateAttachedItems(AttachedItems attachedItems, int slot, ItemStack stack) {
        setData(PZAttachments.ITEM_CONTAINER, attachedItems.with(slot, stack));
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (!isItemValid(slot, stack)) {
            return;
        }
        AttachedItems attachedItems = getAttachedItems();
        if (!ItemStack.matches(attachedItems.getOrDefault(slot), stack)) {
            updateAttachedItems(attachedItems, slot, stack);
        }
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return getAttachedItems().getOrDefault(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack toInsert, boolean simulate) {
        if (slot < 0 || slot >= getSlots() || toInsert.isEmpty() || !isItemValid(slot, toInsert)) {
            return toInsert;
        }
        AttachedItems attachedItems = getAttachedItems();
        ItemStack existing = attachedItems.getOrDefault(slot);
        int insertLimit = Math.min(getSlotLimit(slot), toInsert.getMaxStackSize());
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
            updateAttachedItems(attachedItems, slot, toInsert.copyWithCount(existing.getCount() + inserted));
        }
        return toInsert.copyWithCount(toInsert.getCount() - inserted);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= getSlots() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        AttachedItems attachedItems = getAttachedItems();
        ItemStack existing = attachedItems.getOrDefault(slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int toExtract = Math.min(amount, existing.getCount());
        if (!simulate) {
            updateAttachedItems(attachedItems, slot, existing.copyWithCount(existing.getCount() - toExtract));
        }

        return existing.copyWithCount(toExtract);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 99;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.canFitInsideContainerItems() && slot == 0 && stack.getBurnTime(RecipeType.SMELTING) > 0;
    }
}