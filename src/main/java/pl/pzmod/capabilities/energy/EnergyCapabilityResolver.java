package pl.pzmod.capabilities.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.energy.ISidedEnergyHandler;
import pl.pzmod.items.PZItem;

import java.util.Objects;

public class EnergyCapabilityResolver implements ISidedEnergyHandler {
    private final ISidedEnergyHandler handler;
    private final @Nullable Direction side;

    public static ISidedEnergyHandler forBlockEntity(@NotNull PZBlockEntity blockEntity, @Nullable Direction side) {
        return new EnergyCapabilityResolver(Objects.requireNonNull(blockEntity.getEnergyHandler(blockEntity)), side);
    }

    public static ISidedEnergyHandler forItem(@NotNull ItemStack stack, @NotNull PZItem item) {
        return new EnergyCapabilityResolver(Objects.requireNonNull(item.getEnergyHandler(stack)), null);
    }

    private EnergyCapabilityResolver(@NotNull ISidedEnergyHandler handler, @Nullable Direction side) {
        this.handler = handler;
        this.side = side;
    }

    @Override
    public @Nullable Direction getDefaultSide() {
        return side;
    }

    @Override
    public int getStorages(@Nullable Direction side) {
        return handler.getStorages(side);
    }

    @Override
    public int insertEnergy(int storage, int energy, @Nullable Direction side, @NotNull Action action) {
        return handler.insertEnergy(storage, energy, side, action);
    }

    @Override
    public int extractEnergy(int storage, int energy, @Nullable Direction side, @NotNull Action action) {
        return handler.extractEnergy(storage, energy, side, action);
    }

    @Override
    public int getEnergy(int storage, @Nullable Direction side) {
        return handler.getEnergy(storage, side);
    }

    @Override
    public int getEnergyCapacity(int storage, @Nullable Direction side) {
        return handler.getEnergyCapacity(storage, side);
    }

    @Override
    public boolean canInsert(int storage, @Nullable Direction side) {
        return handler.canInsert(storage, side);
    }

    @Override
    public boolean canExtract(int storage, @Nullable Direction side) {
        return handler.canExtract(storage, side);
    }
}
