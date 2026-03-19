package pl.pzmod.attachments.containers.creator;

import net.minecraft.world.item.ItemStack;
import pl.pzmod.attachments.containers.ContainerType;

@FunctionalInterface
public interface IBasicContainerCreator<C> {
    C create(ContainerType<? super C, ?, ?> containerType, ItemStack attachedTo, int containerIndex);
}
