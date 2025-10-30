package pl.pzmod.capabilities.item;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemItemCapabilityResolver extends ItemCapabilityResolver<Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemItemCapabilityResolver(ItemStack stack, Void context) {
        super(stack, context, ((IItemHolder) stack.getItem()).getSlotCount(),
                ((IItemHolder) stack.getItem()).getSlotLimit(), alwaysTrue, alwaysTrue);
    }
}
