package pl.pzmod.data.containers;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.ContainerType;

public interface IAttachmentHolder<A extends IAttachedContainers<?, A>> {
    static <A extends IAttachedContainers<?, A>> IAttachmentHolder<A> from(ItemStack stack, int containerCount) {
        return new IAttachmentHolder<>() {
            @Override
            public @NotNull A createNewAttachment(@NotNull ContainerType<A> type) {
                return type.emptyValue().withSize(containerCount);
            }

            @Override
            public @NotNull A getOrEmpty(@NotNull ContainerType<A> type) {
                return stack.getOrDefault(type.dataComponent(), type.emptyValue());
            }

            @Override
            public void set(@NotNull ContainerType<A> type, @NotNull A attached) {
                stack.set(type.dataComponent(), attached);
            }
        };
    }

    static <A extends IAttachedContainers<?, A>> IAttachmentHolder<A> from(BlockEntity blockEntity, int containerCount) {
        return new IAttachmentHolder<>() {
            @Override
            public @NotNull A createNewAttachment(@NotNull ContainerType<A> type) {
                return type.emptyValue().withSize(containerCount);
            }

            @Override
            public @NotNull A getOrEmpty(@NotNull ContainerType<A> type) {
                return blockEntity.getExistingData(type.attachment()).orElse(type.emptyValue());
            }

            @Override
            public void set(@NotNull ContainerType<A> type, @NotNull A attached) {
                blockEntity.setData(type.attachment(), attached);
            }
        };
    }

    @NotNull
    A createNewAttachment(@NotNull ContainerType<A> type);

    @NotNull
    A getOrEmpty(@NotNull ContainerType<A> type);

    void set(@NotNull ContainerType<A> type, @NotNull A attached);
}
