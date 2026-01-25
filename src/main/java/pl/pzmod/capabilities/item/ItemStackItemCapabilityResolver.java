package pl.pzmod.capabilities.item;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemStackItemCapabilityResolver extends ItemCapabilityResolver<ItemStack, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackItemCapabilityResolver(ItemStack stack, Void context) {
        super(stack, context, alwaysTrue, alwaysTrue, ((IItemHolder) stack.getItem()).getSlots(),
                ((IItemHolder) stack.getItem()).getLimit());
    }
}
