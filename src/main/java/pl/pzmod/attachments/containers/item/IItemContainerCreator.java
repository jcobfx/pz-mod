package pl.pzmod.attachments.containers.item;

import net.minecraft.world.item.ItemStack;
import pl.pzmod.attachments.containers.creator.IBaseContainerCreator;

public interface IItemContainerCreator<C extends AttachedItemContainer> extends IBaseContainerCreator<ItemStack, AttachedItems, C> {
}
