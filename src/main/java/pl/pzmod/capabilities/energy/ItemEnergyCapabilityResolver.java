package pl.pzmod.capabilities.energy;

import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ItemEnergyCapabilityResolver
        extends EnergyCapabilityResolver<Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemEnergyCapabilityResolver(ItemStack stack, Void context) {
        super(stack, context, ((IEnergyHolder) stack.getItem()).getEnergyCapacity(),
                ((IEnergyHolder) stack.getItem()).getEnergyMaxTransfer(), alwaysTrue, alwaysTrue);
    }
}
