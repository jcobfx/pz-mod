package pl.pzmod.attachments.containers.creator;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.IAttachedContainers;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface IBaseContainerCreator<T, A extends IAttachedContainers<T, A>, C> {
    @NotNull
    C create(int containerIndex, Supplier<A> attachedGetter, Consumer<A> attachedSetter, Supplier<A> attachedCreator);
}
