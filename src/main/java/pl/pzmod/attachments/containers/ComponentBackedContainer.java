package pl.pzmod.attachments.containers;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class ComponentBackedContainer<T, A extends IAttachedContainers<T, A>> {
    private final ItemStack attachedTo;
    private final int containerIndex;

    protected ComponentBackedContainer(ItemStack attachedTo, int containerIndex) {
        this.attachedTo = attachedTo;
        this.containerIndex = containerIndex;
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
            attached = containerType.createNewAttachment(attachedTo);
            if (attached.isEmpty()) {
                return;
            }
        }
        containerType.set(attachedTo, attached.withContent(containerIndex, copy(contents)));
    }
}
