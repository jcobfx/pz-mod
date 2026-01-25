package pl.pzmod.capabilities.energy;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemStackEnergyCapabilityResolver extends EnergyCapabilityResolver<ItemStack, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackEnergyCapabilityResolver(ItemStack stack, Void context) {
        super(stack, context, alwaysTrue, alwaysTrue, ((IEnergyHolder) stack.getItem()).getEnergyCapacity(),
                ((IEnergyHolder) stack.getItem()).getEnergyMaxTransfer());
    }
}
