package pl.pzmod.capabilities.holder.item;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.holder.ContainerHolder;
import pl.pzmod.capabilities.item.IItemContainer;

import java.util.List;
import java.util.function.Supplier;

public class ItemHolder extends ContainerHolder<IItemContainer> implements IItemHolder {
    public ItemHolder(Supplier<Direction> facingSupplier) {
        super(facingSupplier);
    }

    void addItemContainer(@NotNull IItemContainer container, RelativeSide... sides) {
        addContainerInternal(container, sides);
    }

    @Override
    public @NotNull List<IItemContainer> getItemContainers(@Nullable Direction side) {
        return getContainers(side);
    }
}
