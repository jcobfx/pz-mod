package pl.pzmod.attachments.containers;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class AttachmentBackedContainer<T, A extends IAttachedContainers<T, A>> {
    private final BlockEntity attachedTo;
    private final int containerIndex;
    private final Supplier<A> newAttachmentCreator;

    protected AttachmentBackedContainer(BlockEntity attachedTo, int containerIndex, Supplier<A> newAttachmentCreator) {
        this.attachedTo = attachedTo;
        this.containerIndex = containerIndex;
        this.newAttachmentCreator = newAttachmentCreator;
    }

    protected abstract @NotNull T copy(T toCopy);

    protected abstract @NotNull ContainerType<?, A, ?> getContainerType();

    public @NotNull A getAttached() {
        return getContainerType().getOrEmpty(attachedTo);
    }

    public T getContents(@NotNull A attached) {
        return attached.getOrDefault(containerIndex);
    }

    public void setContents(@NotNull A attached, @NotNull T contents) {
        var containerType = getContainerType();
        if (attached.isEmpty()) {
            attached = newAttachmentCreator.get();
            if (attached.isEmpty()) {
                return;
            }
        }
        containerType.set(attachedTo, attached.withContent(containerIndex, copy(contents)));
    }
}
