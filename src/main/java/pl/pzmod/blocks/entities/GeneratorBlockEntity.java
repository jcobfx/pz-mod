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
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.ContainerHandlerHelper;
import pl.pzmod.capabilities.energy.EnergyContainerConfig;
import pl.pzmod.capabilities.items.ItemContainerConfig;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.energy.EnergyHandler;
import pl.pzmod.data.containers.items.ItemHandler;
import pl.pzmod.registries.PZBlockEntities;
import pl.pzmod.screen.generator.GeneratorMenu;

import java.util.Objects;

public class GeneratorBlockEntity extends PZBlockEntity implements MenuProvider {
    private static final int ENERGY_PER_TICK = 20;

    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity be) {
        if (level.isClientSide()) return;
        var energyCap = Capabilities.ENERGY.getCapability(level, pos, state, be, null);
        var itemCap = Capabilities.ITEM.getCapability(level, pos, state, be, null);
        if (energyCap == null || itemCap == null) {
            return;
        }

        boolean changed = false;
        if (be.burnTime > 0) {
            be.burnTime--;
            energyCap.receiveEnergy(ENERGY_PER_TICK, false);
            changed = true;
        }

        if (be.burnTime <= 0 && energyCap.getEnergyStored() < energyCap.getMaxEnergyStored()) {
            ItemStack fuelStack = itemCap.getStackInSlot(0);
            if (!fuelStack.isEmpty()) {
                int burnTicks = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTicks > 0) {
                    be.burnTime = burnTicks;
                    be.burnTimeTotal = burnTicks;
                    itemCap.extractItem(0, 1, false);
                    changed = true;
                }
            }
        }

        if (changed) {
            be.setChanged();
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

    @Override
    public @NotNull EnergyHandler getInitialEnergyHandler(@NotNull BlockEntity blockEntity) {
        return ContainerHandlerHelper.builder(new EnergyHandler(this::getFacing), IContainerHolder.from(blockEntity, 1))
                .addContainer(EnergyContainerConfig.output(() -> 100_000, () -> 1000))
                .build();
    }

    @Override
    public boolean canHandleEnergy() {
        return true;
    }

    @Override
    public @NotNull ItemHandler getInitialItemHandler(@NotNull BlockEntity blockEntity) {
        return ContainerHandlerHelper.builder(new ItemHandler(this::getFacing), IContainerHolder.from(blockEntity, 1))
                .addContainer(ItemContainerConfig.input(stack -> stack.getBurnTime(RecipeType.SMELTING) > 0, () -> 100))
                .build();
    }

    @Override
    public boolean canHandleItems() {
        return true;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.pz_mod.generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player player) {
        var level = Objects.requireNonNull(getLevel());
        var pos = getBlockPos();
        var state = getBlockState();
        var blockEntity = level.getBlockEntity(pos);
        return new GeneratorMenu(i,
                playerInventory,
                Objects.requireNonNull(Capabilities.ITEM.getCapability(level, pos, state, blockEntity, null)),
                Objects.requireNonNull(Capabilities.ENERGY.getCapability(level, pos, state, blockEntity, null)),
                this.data,
                ContainerLevelAccess.create(level, pos));
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
}