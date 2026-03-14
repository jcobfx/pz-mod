package pl.pzmod.data.containers;

import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface IAttachedContainers<T, A extends IAttachedContainers<T, A>> extends Iterable<T> {
    @NotNull A create(@NotNull List<T> contents);

    @NotNull
    List<T> contents();

    @NotNull
    T getDefault();

    default int size() {
        return contents().size();
    }

    default boolean isEmpty() {
        return contents().isEmpty();
    }

    default T get(int index) {
        return contents().get(index);
    }

    default @NotNull T getOrDefault(int index) {
        var contents = contents();
        if (index >= 0 && index < contents.size()) {
            return contents.get(index);
        } else {
            return getDefault();
        }
    }

    default @NotNull A withContent(int index, @NotNull T content) {
        var contents = contents();
        List<T> copy = new ArrayList<>(contents);
        copy.set(index, content);
        return create(copy);
    }

    default @NotNull A withSize(int size) {
        return create(NonNullList.withSize(size, getDefault()));
    }

    @Override
    default @NotNull Iterator<T> iterator() {
        return contents().iterator();
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        contents().forEach(action);
    }

    @Override
    default Spliterator<T> spliterator() {
        return contents().spliterator();
    }
}
