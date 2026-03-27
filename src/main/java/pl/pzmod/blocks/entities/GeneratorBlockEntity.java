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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.holder.energy.EnergyHolderHelper;
import pl.pzmod.capabilities.holder.energy.IEnergyHolder;
import pl.pzmod.capabilities.holder.item.IItemHolder;
import pl.pzmod.capabilities.holder.item.ItemHolderHelper;
import pl.pzmod.menus.generator.GeneratorMenu;
import pl.pzmod.registries.PZBlocks;

import java.util.Objects;

public class GeneratorBlockEntity extends PZBlockEntity implements MenuProvider {
    private static final int ENERGY_PER_TICK = 20;

    public static void tickServer(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity blockEntity) {
        if (level.isClientSide() || !blockEntity.hasEnergyContainers() || !blockEntity.hasItemContainers()) {
            return;
        }
        var energyContainerOpt = blockEntity.getEnergyContainer(0, null);
        var itemContainerOpt = blockEntity.getItemContainer(0, null);
        if (energyContainerOpt.isEmpty() || itemContainerOpt.isEmpty()) {
            return;
        }
        var energyContainer = energyContainerOpt.get();
        var itemContainer = itemContainerOpt.get();
        boolean changed = false;
        if (blockEntity.burnTime > 0) {
            blockEntity.burnTime--;
            energyContainer.insert(ENERGY_PER_TICK, Action.EXECUTE, AutomationType.INTERNAL);
            changed = true;
        }

        if (blockEntity.burnTime <= 0 && energyContainer.getEnergy() < energyContainer.getMaxEnergy()) {
            ItemStack fuelStack = itemContainer.getItem();
            if (!fuelStack.isEmpty()) {
                int burnTicks = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTicks > 0) {
                    blockEntity.burnTime = burnTicks;
                    blockEntity.burnTimeTotal = burnTicks;
                    itemContainer.extract(0, Action.EXECUTE, AutomationType.INTERNAL);
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
        super(PZBlocks.GENERATOR, pos, blockState);
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
    protected @NotNull IEnergyHolder getInitialEnergyHolder() {
        return EnergyHolderHelper.forSide(this::getFacing, this)
                .addBasic(ConstantPredicates.alwaysTrue(), AutomationType::notExternal, () -> 100_000, () -> 1000)
                .build();
    }

    @Override
    protected @NotNull IItemHolder getInitialItemHolder() {
        return ItemHolderHelper.forSide(this::getFacing, this)
                .addFuelSlot()
                .build();
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