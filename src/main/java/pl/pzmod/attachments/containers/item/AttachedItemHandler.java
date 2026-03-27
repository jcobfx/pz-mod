package pl.pzmod.attachments.containers.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.AttachedHandler;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.item.IPZItemHandler;

import java.util.List;
import java.util.Optional;

public class AttachedItemHandler extends AttachedHandler<AttachedItems, IItemContainer> implements IPZItemHandler {
    public AttachedItemHandler(ItemStack attachedTo, int totalContainers) {
        super(attachedTo, totalContainers);
    }

    @Override
    protected @NotNull ContainerType<IItemContainer, AttachedItems, ?> containerType() {
        return ContainerType.ITEM;
    }

    @Override
    public @NotNull List<IItemContainer> getItemContainers(@Nullable Direction side) {
        return getContainers();
    }

    @Override
    public Optional<IItemContainer> getItemContainer(int slot, @Nullable Direction side) {
        return Optional.of(getContainer(slot));
    }
}
