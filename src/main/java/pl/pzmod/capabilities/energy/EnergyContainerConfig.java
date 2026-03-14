package pl.pzmod.capabilities.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.IContainerConfig;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.energy.IEnergyContainer;
import pl.pzmod.data.containers.energy.EnergyContainer;
import pl.pzmod.utils.ConstantPredicates;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public record EnergyContainerConfig(Predicate<@NotNull AutomationType> canInsert,
                                    Predicate<@NotNull AutomationType> canExtract,
                                    IntSupplier capacity,
                                    IntSupplier rate) implements IContainerConfig<IEnergyContainer> {
    public static EnergyContainerConfig inout(IntSupplier capacity, IntSupplier rate) {
        return new EnergyContainerConfig(ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), capacity, rate);
    }

    public static EnergyContainerConfig input(IntSupplier capacity, IntSupplier rate) {
        return new EnergyContainerConfig(ConstantPredicates.alwaysTrue(), AutomationType::notExternal, capacity, rate);
    }

    public static EnergyContainerConfig output(IntSupplier capacity, IntSupplier rate) {
        return new EnergyContainerConfig(AutomationType::notExternal, ConstantPredicates.alwaysTrue(), capacity, rate);
    }

    @Override
    public IEnergyContainer createContainer(IContainerHolder holder, int index) {
        return new EnergyContainer(holder, index, canInsert, canExtract, capacity, rate);
    }
}
