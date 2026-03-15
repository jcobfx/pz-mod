package pl.pzmod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.registration.BlockEntityTypeRegistryObject;

public interface IEntityBlock<E extends BlockEntity> extends EntityBlock {
    @NotNull
    BlockEntityTypeRegistryObject<? extends E> getBlockEntityType();

    @Override
    default @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return getBlockEntityType().get().create(blockPos, blockState);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(@NotNull Level level,
                                                                             @NotNull BlockState state,
                                                                             @NotNull BlockEntityType<T> blockEntityType) {
        var type = getBlockEntityType();
        return blockEntityType == type.get() ? (BlockEntityTicker<T>) type.getTicker(level.isClientSide) : null;
    }
}
