package pl.pzmod.attachments.containers;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class AttachedHandler<A extends IAttachedContainers<?, A>, C> {
    private final ItemStack attachedTo;
    private final int totalContainers;

    private @Nullable List<C> containers;
    private int numNotInitialized;

    protected AttachedHandler(ItemStack attachedTo, int totalContainers) {
        this.attachedTo = attachedTo;
        this.totalContainers = totalContainers;
    }

    protected abstract @NotNull ContainerType<C, A, ?> containerType();

    public @NotNull List<C> getContainers() {
        List<C> cs = containers();
        for (int i = 0, size = cs.size(); numNotInitialized > 0 && i < size; i++) {
            if (cs.get(i) == null) {
                initializeContainer(i);
            }
        }
        return cs;
    }

    protected @NotNull C getContainer(int index) {
        C container = containers().get(index);
        return container == null ? initializeContainer(index) : container;
    }

    @SuppressWarnings("unchecked")
    private List<C> containers() {
        if (containers == null) {
            containers = Arrays.asList((C[]) new Object[totalContainers]);
            numNotInitialized = totalContainers;
        }
        return containers;
    }

    private C initializeContainer(int index) {
        C container = containerType().createContainer(attachedTo, index);
        containers().set(index, container);
        numNotInitialized--;
        return container;
    }
}
