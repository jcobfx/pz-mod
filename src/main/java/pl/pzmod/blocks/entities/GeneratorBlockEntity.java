package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import pl.pzmod.registries.PZBlockEntities;
import pl.pzmod.screen.generator.GeneratorMenu;

import java.util.Objects;

public class GeneratorBlockEntity extends PZBlockEntity implements MenuProvider {
    private static final int ENERGY_PER_TICK = 20;

    private final ContainerData data;

    private int burnTime = 0;
    private int burnTimeTotal = 0;

    public GeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(PZBlockEntities.GENERATOR.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> {
                        var energyStorage = GeneratorBlockEntity.this.getEnergyStorage(null);
                        if (energyStorage != null) {
                            yield energyStorage.getEnergyStored();
                        } else {
                            yield 0;
                        }
                    }
                    case 1 -> {
                        var energyStorage = GeneratorBlockEntity.this.getEnergyStorage(null);
                        if (energyStorage != null) {
                            yield energyStorage.getMaxEnergyStored();
                        } else {
                            yield 0;
                        }
                    }
                    case 2 -> GeneratorBlockEntity.this.burnTime;
                    case 3 -> GeneratorBlockEntity.this.burnTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 2 -> GeneratorBlockEntity.this.burnTime = value;
                    case 3 -> GeneratorBlockEntity.this.burnTimeTotal = value;
                    default -> {
                        // No other values should be set from the client, so we ignore them.
                    }
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.pz_mod.generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new GeneratorMenu(i,
                playerInventory,
                Objects.requireNonNull(getItemHandler(null)),
                this.data,
                ContainerLevelAccess.create(Objects.requireNonNull(getLevel()), getBlockPos()));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity be) {
        if (level.isClientSide()) return;
        var energyStorage = be.getEnergyStorage(null);
        var inventory = be.getItemHandler(null);
        if (energyStorage == null || inventory == null) {
            return;
        }

        boolean changed = false;
        if (be.burnTime > 0) {
            be.burnTime--;
            energyStorage.receiveEnergy(ENERGY_PER_TICK, false);
            changed = true;
        }

        if (be.burnTime <= 0 && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
            ItemStack fuelStack = inventory.getStackInSlot(0);
            if (!fuelStack.isEmpty()) {
                int burnTicks = fuelStack.getBurnTime(RecipeType.SMELTING);
                if (burnTicks > 0) {
                    be.burnTime = burnTicks;
                    be.burnTimeTotal = burnTicks;
                    inventory.extractItem(0, 1, false);
                    changed = true;
                }
            }
        }

        if (changed) {
            be.setChanged();
        }
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
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}