package pl.pzmod.data.containers.fluids;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FluidHandlerItem extends FluidHandler implements IFluidHandlerItem {
    private final ItemStack stack;

    public FluidHandlerItem(Supplier<Direction> facing, ItemStack stack) {
        super(facing);
        this.stack = stack;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return stack;
    }
}
