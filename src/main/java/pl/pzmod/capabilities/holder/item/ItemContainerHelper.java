package pl.pzmod.capabilities.holder.item;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.item.IItemContainer;
import pl.pzmod.data.containers.IAttachmentHolder;
import pl.pzmod.data.containers.items.AttachedItems;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ItemContainerHelper {
    private final IMutableItemHolder containerHolder;
    private final IAttachmentHolder<AttachedItems> attachmentHolder;

    private int current;
    private boolean built;

    private ItemContainerHelper(IMutableItemHolder containerHolder, IAttachmentHolder<AttachedItems> attachmentHolder) {
        this.containerHolder = containerHolder;
        this.attachmentHolder = attachmentHolder;
        this.current = 0;
        this.built = false;
    }

    public static ItemContainerHelper forSide(Supplier<Direction> facingSupplier, IAttachmentHolder<AttachedItems> attachmentHolder) {
        return new ItemContainerHelper(new ItemHolder(facingSupplier), attachmentHolder);
    }

    public <C extends IItemContainer> C addContainer(BiFunction<IAttachmentHolder<AttachedItems>, Integer, C> containerFactory) {
        checkBuilt();
        var container = containerFactory.apply(attachmentHolder, current++);
        containerHolder.addItemContainer(container);
        return container;
    }

    public <C extends IItemContainer> C addContainer(BiFunction<IAttachmentHolder<AttachedItems>, Integer, C> containerFactory, RelativeSide... sides) {
        checkBuilt();
        var container = containerFactory.apply(attachmentHolder, current++);
        containerHolder.addItemContainer(container, sides);
        return container;
    }

    public IItemHolder build() {
        built = true;
        return containerHolder;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
    }
}
