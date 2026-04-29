package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.menus.GeneratorMenu;
import pl.pzmod.registries.PZBlockEntities;

public class GeneratorBlockEntity extends BaseContainerBlockEntity implements IEnergyStorage {
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
            ItemStack fuelStack = blockEntity.getItem(0);
            if (!fuelStack.isEmpty()) {
                int burnTicks = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTicks > 0) {
                    blockEntity.burnTime = burnTicks;
                    blockEntity.burnTimeTotal = burnTicks;
                    blockEntity.removeItem(0, 1);
                    changed = true;
                }
            }
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }

    private final ContainerData data;
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    // TODO: Change to attached data to store data on block entities.
    private int energy;

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
    protected @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory) {
        return new GeneratorMenu(i, playerInventory, this);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energy);
        tag.putInt("burnTime", burnTime);
        tag.putInt("burnTimeTotal", burnTimeTotal);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.pz_mod.generator");
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        energy = tag.getInt("energy");
        burnTime = tag.getInt("burnTime");
        burnTimeTotal = tag.getInt("burnTimeTotal");
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public int receiveEnergy(int energy, boolean simulate) {
        int space = ENERGY_CAPACITY - this.energy;
        int toReceive = Math.min(space, Math.min(energy, ENERGY_MAX_TRANSFER));
        if (!simulate) {
            this.energy += toReceive;
            setChanged();
        }
        return toReceive;
    }

    @Override
    public int extractEnergy(int energy, boolean simulate) {
        int toExtract = Math.min(this.energy, Math.min(energy, ENERGY_MAX_TRANSFER));
        if (!simulate) {
            this.energy -= toExtract;
            setChanged();
        }
        return toExtract;
    }

    @Override
    public int getEnergyStored() {
        return energy;
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
}