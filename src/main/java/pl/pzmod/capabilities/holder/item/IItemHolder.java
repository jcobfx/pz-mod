package pl.pzmod.capabilities.holder.item;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.item.AttachedItems;
import pl.pzmod.capabilities.holder.IHolder;
import pl.pzmod.capabilities.item.IItemContainer;

import java.util.List;

public interface IItemHolder extends IHolder {
    @NotNull
    List<IItemContainer> getItemContainers(@Nullable Direction side);

    default AttachedItems initAttachedItems() {
        return AttachedItems.create(getItemContainers(null).size());
    }
}
