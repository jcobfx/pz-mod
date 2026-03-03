package pl.pzmod.capabilities.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import pl.pzmod.capabilities.proxy.PZItemProxy;
import pl.pzmod.data.containers.AttachedItems;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class ItemStackItemCapabilityResolver extends ItemCapabilityResolver<ItemStack, DataComponentType<AttachedItems>, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackItemCapabilityResolver(ItemStack stack, Void context) {
        super(new PZItemProxy(stack), PZDataComponents.ITEMS_COMPONENT, context, alwaysTrue, alwaysTrue);
    }
}
