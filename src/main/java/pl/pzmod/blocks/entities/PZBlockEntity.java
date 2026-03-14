package pl.pzmod.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.blocks.GeneratorBlock;
import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.fluids.IFluidHolder;
import pl.pzmod.capabilities.items.IItemHolder;
import pl.pzmod.data.containers.energy.EnergyHandler;
import pl.pzmod.data.containers.fluids.FluidHandler;
import pl.pzmod.data.containers.items.ItemHandler;

public abstract class PZBlockEntity extends BlockEntity implements IEnergyHolder<BlockEntity>, IFluidHolder<BlockEntity>, IItemHolder<BlockEntity> {
//    private final BlockCapabilityCache<PZBlockEntity, @Nullable Direction> capabilityCache;

    private EnergyHandler energyHandler;
    private FluidHandler fluidHandler;
    private ItemHandler itemHandler;

    protected PZBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
//        this.capabilityCache = null;
    }

    protected @NotNull Direction getFacing() {
        return getBlockState().getValue(GeneratorBlock.FACING);
    }

    protected @Nullable EnergyHandler getInitialEnergyHandler(@NotNull BlockEntity blockEntity) {
        return null;
    }

    @Override
    public final @Nullable EnergyHandler getEnergyHandler(@NotNull BlockEntity blockEntity) {
        if (energyHandler == null) {
            energyHandler = getInitialEnergyHandler(blockEntity);
        }
        return energyHandler;
    }

    @Override
    public boolean canHandleEnergy() {
        return false;
    }

    protected @Nullable FluidHandler getInitialFluidHandler(@NotNull BlockEntity blockEntity) {
        return null;
    }

    @Override
    public final @Nullable FluidHandler getFluidHandler(@NotNull BlockEntity blockEntity) {
        if (fluidHandler == null) {
            fluidHandler = getInitialFluidHandler(blockEntity);
        }
        return fluidHandler;
    }

    @Override
    public boolean canHandleFluids() {
        return false;
    }

    protected @Nullable ItemHandler getInitialItemHandler(@NotNull BlockEntity blockEntity) {
        return null;
    }

    @Override
    public final @Nullable ItemHandler getItemHandler(@NotNull BlockEntity blockEntity) {
        if (itemHandler == null) {
            itemHandler = getInitialItemHandler(blockEntity);
        }
        return itemHandler;
    }

    @Override
    public boolean canHandleItems() {
        return false;
    }
}
