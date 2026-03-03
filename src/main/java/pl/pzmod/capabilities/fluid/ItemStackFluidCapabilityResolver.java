package pl.pzmod.capabilities.fluid;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.proxy.PZItemProxy;
import pl.pzmod.data.containers.AttachedFluids;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Predicate;

public class ItemStackFluidCapabilityResolver extends FluidCapabilityResolver<ItemStack, DataComponentType<AttachedFluids>, Void>
        implements IFluidHandlerItem {
    private static final Predicate<Void> alwaysTrue = ctx -> true;

    public ItemStackFluidCapabilityResolver(ItemStack stack, Void context) {
        super(new PZItemProxy(stack), PZDataComponents.FLUIDS_COMPONENT, context, alwaysTrue, alwaysTrue);
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return getDataHolder().getParent();
    }
}
