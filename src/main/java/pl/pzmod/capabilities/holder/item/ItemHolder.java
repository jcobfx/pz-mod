package pl.pzmod.capabilities.holder.item;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.item.AttachedItems;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.attachments.containers.item.IItemContainerCreator;
import pl.pzmod.capabilities.holder.ContainersHolder;
import pl.pzmod.attachments.containers.item.IItemContainer;
import pl.pzmod.registries.PZAttachments;

import java.util.List;
import java.util.function.Supplier;

public class ItemHolder extends ContainersHolder<IItemContainer> implements IItemHolder {
    private final BlockEntity blockEntity;
    private int size;

    public ItemHolder(Supplier<Direction> facingSupplier, BlockEntity blockEntity) {
        super(facingSupplier);
        this.blockEntity = blockEntity;
    }

    void addItemContainer(IItemContainerCreator<? extends IItemContainer> creator, RelativeSide... sides) {
        addContainerInternal(creator.create(size++, this::getAttachedItems, this::setAttachedItems, this::create), sides);
    }

    private AttachedItems getAttachedItems() {
        return blockEntity.getExistingData(PZAttachments.ITEMS_ATTACHMENT).orElse(AttachedItems.EMPTY);
    }

    private void setAttachedItems(AttachedItems attachedItems) {
        blockEntity.setData(PZAttachments.ITEMS_ATTACHMENT, attachedItems);
    }

    private AttachedItems create() {
        return AttachedItems.create(size);
    }

    @Override
    public @NotNull List<IItemContainer> getItemContainers(@Nullable Direction side) {
        return getContainers(side);
    }
}
