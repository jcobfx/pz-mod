package pl.pzmod.data.containers;

import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public sealed interface IContainerHolder {
    static IContainerHolder from(MutableDataComponentHolder holder, int count) {
        return new ComponentBackedHolder(holder, count);
    }

    static IContainerHolder from(IAttachmentHolder holder, int count) {
        return new AttachmentBackedHolder(holder, count);
    }

    int getContainerCount();

    <A extends IAttachedContainers<?, A>> Optional<A> getContainers(@NotNull ContainerType<A> type);

    <A extends IAttachedContainers<?, A>> void setContainers(@NotNull ContainerType<A> type, @NotNull A value);

    record ComponentBackedHolder(MutableDataComponentHolder holder, int count) implements IContainerHolder {
        @Override
        public int getContainerCount() {
            return count;
        }

        @Override
        public <A extends IAttachedContainers<?, A>> Optional<A> getContainers(@NotNull ContainerType<A> type) {
            return Optional.ofNullable(holder.get(type.getDataComponent()));
        }

        @Override
        public <A extends IAttachedContainers<?, A>> void setContainers(@NotNull ContainerType<A> type, @NotNull A value) {
            holder.set(type.getDataComponent(), value);
        }
    }

    record AttachmentBackedHolder(IAttachmentHolder holder, int count) implements IContainerHolder {
        @Override
        public int getContainerCount() {
            return count;
        }

        @Override
        public <A extends IAttachedContainers<?, A>> Optional<A> getContainers(@NotNull ContainerType<A> type) {
            return holder.getExistingData(type.getAttachment());
        }

        @Override
        public <A extends IAttachedContainers<?, A>> void setContainers(@NotNull ContainerType<A> type, @NotNull A value) {
            holder.setData(type.getAttachment(), value);
        }
    }
}
