package pl.pzmod.attachments.containers.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.ComponentBackedHandler;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.item.IItemContainer;
import pl.pzmod.capabilities.item.IPZItemHandler;

import java.util.List;
import java.util.Optional;

public class ComponentBackedItemHandler extends ComponentBackedHandler<IItemContainer, ItemStack, AttachedItems> implements IPZItemHandler {
    public ComponentBackedItemHandler(ItemStack attachedTo, int totalContainers) {
        super(attachedTo, totalContainers);
    }

    @Override
    protected ContainerType<IItemContainer, AttachedItems, ?> containerType() {
        return ContainerType.ITEMS;
    }

    @Override
    public @NotNull List<IItemContainer> getItemContainers(@Nullable Direction side) {
        return getContainers();
    }

    @Override
    public Optional<IItemContainer> getItemContainer(int slot, @Nullable Direction side) {
        return Optional.of(getContainer(slot));
    }

    @Override
    public int getSlots(@Nullable Direction side) {
        return totalContainers();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot, @Nullable Direction side) {
        return getContents(slot);
    }
}
