package pl.pzmod.containers.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ItemEnergyHandler extends BasicEnergyHandler<ItemStack> {
    private static final Predicate<@Nullable Direction> alwaysTrue = s -> true;

    public ItemEnergyHandler(ItemStack holder,
                             int capacity,
                             int maxTransfer) {
        super(holder, capacity, maxTransfer, alwaysTrue, alwaysTrue);
    }
}
