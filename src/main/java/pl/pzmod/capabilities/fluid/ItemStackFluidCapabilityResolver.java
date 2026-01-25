package pl.pzmod.capabilities.fluid;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemStackFluidCapabilityResolver extends FluidCapabilityResolver<ItemStack, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackFluidCapabilityResolver(ItemStack stack, Void context) {
        super(stack, context, alwaysTrue, alwaysTrue, ((IFluidHolder) stack.getItem()).getTanks(),
                ((IFluidHolder) stack.getItem()).getCapacity(), ((IFluidHolder) stack.getItem()).getValidator());
    }
}
