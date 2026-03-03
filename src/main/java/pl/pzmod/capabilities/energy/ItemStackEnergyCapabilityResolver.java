package pl.pzmod.capabilities.energy;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import pl.pzmod.capabilities.proxy.PZItemProxy;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class ItemStackEnergyCapabilityResolver extends EnergyCapabilityResolver<ItemStack, DataComponentType<AttachedEnergy>, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackEnergyCapabilityResolver(ItemStack stack, Void context) {
        super(new PZItemProxy(stack), PZDataComponents.ENERGY_COMPONENT, context, alwaysTrue, alwaysTrue);
    }
}
