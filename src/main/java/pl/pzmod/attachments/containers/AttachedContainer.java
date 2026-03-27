package pl.pzmod.attachments.containers;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AttachedContainer<T, A extends IAttachedContainers<T, A>> {
    private final int index;
    private final Supplier<@NotNull A> attachedGetter;
    private final Consumer<@NotNull A> attachedSetter;
    private final Supplier<@NotNull A> attachedCreator;

    protected AttachedContainer(int index,
                                Supplier<@NotNull A> attachedGetter,
                                Consumer<@NotNull A> attachedSetter,
                                Supplier<@NotNull A> attachedCreator) {
        this.index = index;
        this.attachedGetter = attachedGetter;
        this.attachedSetter = attachedSetter;
        this.attachedCreator = attachedCreator;
    }

    public @NotNull A getAttached() {
        return attachedGetter.get();
    }

    public @NotNull T getContents(@NotNull A attached) {
        return attached.getOrDefault(index);
    }

    public void setContents(@NotNull A attached, @NotNull T stack) {
        if (attached.isEmpty()) {
            attached = attachedCreator.get();
            if (attached.isEmpty()) {
                return;
            }
        }
        attachedSetter.accept(attached.withContent(index, stack));
    }
}
