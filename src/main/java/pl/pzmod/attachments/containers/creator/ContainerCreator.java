package pl.pzmod.attachments.containers.creator;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.IAttachedContainers;
import pl.pzmod.attachments.containers.AttachedContainer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ContainerCreator<T, A extends IAttachedContainers<T, A>, C extends AttachedContainer<T, A>> implements IContainerCreator<T, A, C> {
    private final List<IBaseContainerCreator<T, A, ? extends C>> creators;

    protected ContainerCreator(List<IBaseContainerCreator<T, A, ? extends C>> creators) {
        this.creators = List.copyOf(creators);
    }

    @Override
    public int totalContainers() {
        return creators.size();
    }

    @Override
    public @NotNull C create(int containerIndex, Supplier<A> attachedGetter, Consumer<A> attachedSetter, Supplier<A> attachedCreator) {
        return creators.get(containerIndex).create(containerIndex, attachedGetter, attachedSetter, attachedCreator);
    }
}
