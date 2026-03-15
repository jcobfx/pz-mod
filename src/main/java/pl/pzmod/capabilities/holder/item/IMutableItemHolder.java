package pl.pzmod.capabilities.holder.item;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.item.IItemContainer;

public interface IMutableItemHolder extends IItemHolder {
    void addItemContainer(@NotNull IItemContainer container, RelativeSide... sides);
}
