package pl.pzmod.capabilities.fluids;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.IContainerConfig;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.fluids.FluidContainer;
import pl.pzmod.data.containers.fluids.IFluidContainer;
import pl.pzmod.utils.ConstantPredicates;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public record FluidContainerConfig(Predicate<@NotNull FluidStack> validator,
                                   Predicate<@NotNull AutomationType> canInsert,
                                   Predicate<@NotNull AutomationType> canExtract,
                                   IntSupplier capacity,
                                   IntSupplier rate) implements IContainerConfig<IFluidContainer> {
    public static FluidContainerConfig inout(Predicate<@NotNull FluidStack> validator, IntSupplier capacity, IntSupplier rate) {
        return new FluidContainerConfig(validator, ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), capacity, rate);
    }

    public static FluidContainerConfig input(Predicate<@NotNull FluidStack> validator, IntSupplier capacity, IntSupplier rate) {
        return new FluidContainerConfig(validator, ConstantPredicates.alwaysTrue(), AutomationType::notExternal, capacity, rate);
    }

    public static FluidContainerConfig output(Predicate<@NotNull FluidStack> validator, IntSupplier capacity, IntSupplier rate) {
        return new FluidContainerConfig(validator, AutomationType::notExternal, ConstantPredicates.alwaysTrue(), capacity, rate);
    }

    @Override
    public IFluidContainer createContainer(IContainerHolder holder, int index) {
        return new FluidContainer(holder, index, validator, canInsert, canExtract, capacity, rate);
    }
}
