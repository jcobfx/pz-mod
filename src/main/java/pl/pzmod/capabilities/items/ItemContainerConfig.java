package pl.pzmod.capabilities.items;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.IContainerConfig;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.items.IItemContainer;
import pl.pzmod.data.containers.items.ItemContainer;
import pl.pzmod.utils.ConstantPredicates;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public record ItemContainerConfig(Predicate<@NotNull ItemStack> validator,
                                  Predicate<@NotNull AutomationType> canInsert,
                                  Predicate<@NotNull AutomationType> canExtract,
                                  IntSupplier capacity) implements IContainerConfig<IItemContainer> {
    public static ItemContainerConfig inout(Predicate<@NotNull ItemStack> validator, IntSupplier capacity) {
        return new ItemContainerConfig(validator, ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), capacity);
    }

    public static ItemContainerConfig input(Predicate<@NotNull ItemStack> validator, IntSupplier capacity) {
        return new ItemContainerConfig(validator, ConstantPredicates.alwaysTrue(), AutomationType::notExternal, capacity);
    }

    public static ItemContainerConfig output(Predicate<@NotNull ItemStack> validator, IntSupplier capacity) {
        return new ItemContainerConfig(validator, AutomationType::notExternal, ConstantPredicates.alwaysTrue(), capacity);
    }

    @Override
    public IItemContainer createContainer(IContainerHolder holder, int index) {
        return new ItemContainer(holder, index, validator, canInsert, canExtract, capacity);
    }
}
