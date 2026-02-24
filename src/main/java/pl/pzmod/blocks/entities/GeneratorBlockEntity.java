package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.registries.PZBlockEntities;
import pl.pzmod.screen.generator.GeneratorMenu;

public class GeneratorBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private int energyStored = 0;
    private final int capacity = 100_000;
    private final int maxTransfer = 1_000;

    private int burnTime = 0;
    private int burnTimeTotal = 0;
    private final int energyPerTick = 20;

    protected final ContainerData data;

    public GeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(PZBlockEntities.GENERATOR_BE.get(), pos, blockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GeneratorBlockEntity.this.energyStored;
                    case 1 -> GeneratorBlockEntity.this.capacity;
                    case 2 -> GeneratorBlockEntity.this.burnTime;
                    case 3 -> GeneratorBlockEntity.this.burnTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GeneratorBlockEntity.this.energyStored = value;
                    case 2 -> GeneratorBlockEntity.this.burnTime = value;
                    case 3 -> GeneratorBlockEntity.this.burnTimeTotal = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.pzmod.generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GeneratorMenu(i, inventory, this, this.data);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity be) {
        if (level.isClientSide()) return;

        boolean wasBurning = be.burnTime > 0;
        boolean changed = false;

        if (be.burnTime > 0) {
            be.burnTime--;
            if (be.energyStored < be.capacity) {
                be.energyStored = Math.min(be.capacity, be.energyStored + be.energyPerTick);
            }
            changed = true;
        }

        if (be.burnTime <= 0 && be.energyStored < be.capacity) {
            ItemStack fuelStack = be.inventory.getStackInSlot(0);
            if (!fuelStack.isEmpty()) {
                int burnTicks = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTicks > 0) {
                    be.burnTime = burnTicks;
                    be.burnTimeTotal = burnTicks;
                    fuelStack.shrink(1);
                    changed = true;
                }
            }
        }

        if (changed) {
            be.setChanged();
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("energy", energyStored);
        tag.putInt("burnTime", burnTime);
        tag.putInt("burnTimeTotal", burnTimeTotal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        energyStored = tag.getInt("energy");
        burnTime = tag.getInt("burnTime");
        burnTimeTotal = tag.getInt("burnTimeTotal");
    }

    public int getEnergyStored() { return energyStored; }
    public int getCapacity() { return capacity; }
    public void consumeEnergy(int amount) {
        this.energyStored = Math.max(0, this.energyStored - amount);
        setChanged();
    }

    public int getMaxTransfer() {
        return maxTransfer;
    }

    public void setEnergyStored(int amount) {
        this.energyStored = Mth.clamp(amount, 0, capacity);
        setChanged();
    }
}