package pl.pzmod.capabilities.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import pl.pzmod.capabilities.IDataHolder;
import pl.pzmod.data.containers.AttachedItems;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class ItemStackItemCapabilityResolver extends ItemCapabilityResolver<IDataHolder.Item<AttachedItems>, DataComponentType<AttachedItems>, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackItemCapabilityResolver(ItemStack stack, Void context) {
        super(IDataHolder.Item.from(stack), PZDataComponents.ITEMS_COMPONENT, context, alwaysTrue, alwaysTrue);
    }
}
