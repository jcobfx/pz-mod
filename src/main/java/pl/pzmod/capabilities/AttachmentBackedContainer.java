package pl.pzmod.capabilities;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.IAttachedContainers;
import pl.pzmod.data.containers.IAttachmentHolder;

public abstract class AttachmentBackedContainer<T, A extends IAttachedContainers<T, A>> {
    private final IAttachmentHolder<A> holder;
    private final int index;

    protected AttachmentBackedContainer(IAttachmentHolder<A> holder, int index) {
        this.holder = holder;
        this.index = index;
    }

    protected abstract @NotNull ContainerType<A> getContainerType();

    public @NotNull A getAttached() {
        return holder.getOrEmpty(getContainerType());
    }

    public T getContents(@NotNull A attached) {
        return attached.getOrDefault(index);
    }

    public void setContents(@NotNull A attached, @NotNull T contents) {
        var containerType = getContainerType();
        if (attached.isEmpty()) {
            attached = holder.createNewAttachment(containerType);
            if (attached.isEmpty()) {
                return;
            }
        }
        holder.set(containerType, attached.withContent(index, contents));
    }
}
