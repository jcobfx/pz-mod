package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class PZBlockEntity extends BlockEntity {
    protected PZBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public @Nullable IEnergyStorage getEnergyStorage(@Nullable Direction side) {
        var level = getLevel();
        if (level == null) {
            return null;
        }
        return level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos(), side);
    }

    public @Nullable IItemHandler getItemHandler(@Nullable Direction side) {
        var level = getLevel();
        if (level == null) {
            return null;
        }
        return level.getCapability(Capabilities.ItemHandler.BLOCK, getBlockPos(), side);
    }

    public @Nullable IItemHandler getItemHandler(BlockPos pos, BlockState state, @Nullable Direction side) {
        var level = getLevel();
        if (level == null) {
            return null;
        }
        return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, this, side);
    }

    public @Nullable IFluidHandler getFluidHandler(@Nullable Direction side) {
        var level = getLevel();
        if (level == null) {
            return null;
        }
        return level.getCapability(Capabilities.FluidHandler.BLOCK, getBlockPos(), side);
    }
}
