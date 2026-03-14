package pl.pzmod.capabilities.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.data.containers.IContainerHandler;
import pl.pzmod.data.containers.items.IItemContainer;
import pl.pzmod.data.containers.items.ItemHandler;

public interface IItemHolder<H> {
    @Nullable
    ItemHandler getItemHandler(@NotNull H holder);

    boolean canHandleItems();
}
