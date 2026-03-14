package pl.pzmod.data.containers;

import org.jetbrains.annotations.NotNull;

public abstract class BaseContainer<T, A extends IAttachedContainers<T, A>> {
    private final IContainerHolder holder;
    private final int index;

    protected BaseContainer(@NotNull IContainerHolder holder, int index) {
        this.holder = holder;
        this.index = index;
    }

    protected abstract @NotNull ContainerType<A> getContainerType();

    protected @NotNull A getAttached() {
        return getContainerType().getOrEmpty(holder);
    }

    protected @NotNull T getContents(@NotNull A attached) {
        return attached.getOrDefault(index);
    }

    protected void setContents(@NotNull A attached, @NotNull T content) {
        if (attached.isEmpty()) {
            attached = getContainerType().createNewAttachment(holder);
            if (attached.isEmpty()) {
                return;
            }
        }
        getContainerType().set(holder, attached.withContent(index, content));
    }
}
