package pl.pzmod.capabilities.resolver.manager;

import net.neoforged.neoforge.items.IItemHandler;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.holder.item.IItemHolder;
import pl.pzmod.capabilities.item.IItemContainer;
import pl.pzmod.capabilities.proxy.ItemHandlerProxy;
import pl.pzmod.capabilities.item.ISidedItemHandler;

public class ItemHandlerManager extends CapabilityHandlerManager<IItemHolder, IItemContainer, IItemHandler, ISidedItemHandler> {
    public ItemHandlerManager(IItemHolder holder, ISidedItemHandler baseHandler) {
        super(holder, baseHandler, ItemHandlerProxy::new, Capabilities.ITEM.block(), IItemHolder::getItemContainers);
    }
}
