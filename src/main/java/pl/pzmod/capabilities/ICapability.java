package pl.pzmod.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICapability<H, I extends H> {
    BlockCapability<H, @Nullable Direction> block();

    <C extends @Nullable Object> EntityCapability<H, C> entity();

    ItemCapability<I, Void> item();

    default @Nullable H getCapability(@Nullable Level level, @NotNull BlockPos pos, @Nullable Direction side) {
        return getCapability(level, pos, null, null, side);
    }

    default @Nullable H getCapability(@Nullable Level level,
                            @NotNull BlockPos pos,
                            @Nullable BlockState state,
                            @Nullable BlockEntity blockEntity,
                            @Nullable Direction side) {
        return level == null ? null : level.getCapability(block(), pos, state, blockEntity, side);
    }

    default @Nullable H getCapability(@Nullable Entity entity) {
        return entity == null ? null : entity.getCapability(entity(), null);
    }

    default @Nullable I getCapability(@NotNull ItemStack itemStack) {
        return itemStack.getCapability(item());
    }
}
