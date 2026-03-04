package pl.pzmod.capabilities.energy;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import pl.pzmod.capabilities.IDataHolder;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class ItemStackEnergyCapabilityResolver
        extends EnergyCapabilityResolver<IDataHolder.Item<AttachedEnergy>, DataComponentType<AttachedEnergy>, Void> {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackEnergyCapabilityResolver(ItemStack stack, Void context) {
        super(IDataHolder.Item.from(stack), PZDataComponents.ENERGY_COMPONENT, context, alwaysTrue, alwaysTrue);
    }
}
