package pl.pzmod.capabilities.energy;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemEnergyCapabilityResolver
        extends EnergyCapabilityResolver<ItemStack, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemEnergyCapabilityResolver(ItemStack stack, Void ignoredContext) {
        this(stack);
    }

    public ItemEnergyCapabilityResolver(ItemStack stack) {
        super(stack, ((IEnergyHolder) stack.getItem()).getEnergyCapacity(),
                ((IEnergyHolder) stack.getItem()).getEnergyMaxTransfer(), null, alwaysTrue, alwaysTrue);
    }
}
