package pl.pzmod.attachments.containers;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class ComponentBackedHandler<C, T, A extends IAttachedContainers<T, A>> {
    private final ItemStack attachedTo;
    private final int totalContainers;

    private @Nullable List<C> containers;
    private int numNotInitialized;

    protected abstract ContainerType<C, A, ?> containerType();

    protected A getAttached() {
        return containerType().getOrEmpty(attachedTo);
    }

    protected T getContents(int index) {
        return getAttached().getOrDefault(index);
    }

    protected ComponentBackedHandler(ItemStack attachedTo, int totalContainers) {
        this.attachedTo = attachedTo;
        this.totalContainers = totalContainers;
    }

    @SuppressWarnings("unchecked")
    private List<C> getContainersLazy() {
        if (containers == null) {
            containers = Arrays.asList((C[]) new Object[totalContainers]);
            numNotInitialized = totalContainers;
        }
        return containers;
    }

    public List<C> getContainers() {
        List<C> lazyContainers = getContainersLazy();
        for (int i = 0, size = lazyContainers.size(); numNotInitialized > 0 && i < size; i++) {
            if (lazyContainers.get(i) == null) {
                initializeContainer(i);
            }
        }
        return lazyContainers;
    }

    private @NotNull C initializeContainer(int index) {
        C container = containerType().createContainer(attachedTo, index);
        getContainersLazy().set(index, container);
        numNotInitialized--;
        return container;
    }

    protected @NotNull C getContainer(int index) {
        C container = getContainersLazy().get(index);
        return container == null ? initializeContainer(index) : container;
    }

    public int totalContainers() {
        return totalContainers;
    }
}
