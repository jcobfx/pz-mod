package pl.pzmod.attachments.containers.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.creator.ContainerCreator;
import pl.pzmod.attachments.containers.creator.IBaseContainerCreator;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.ConstantPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class EnergyContainersBuilder {
    public static EnergyContainersBuilder builder() {
        return new EnergyContainersBuilder();
    }

    private final List<IBaseContainerCreator<Integer, AttachedEnergy, ? extends AttachedEnergyContainer>> containerCreators;

    private EnergyContainersBuilder() {
        this.containerCreators = new ArrayList<>();
    }

    public ContainerCreator<Integer, AttachedEnergy, AttachedEnergyContainer> build() {
        return new BaseEnergyContainerCreator(containerCreators);
    }

    public EnergyContainersBuilder addBasic(IntSupplier maxEnergy, IntSupplier rate) {
        return addStorage((containerIndex, getter, setter, creator) ->
                new AttachedEnergyContainer(containerIndex, getter, setter, creator,
                        AutomationType::manual, ConstantPredicates.alwaysTrue(), maxEnergy, rate));
    }

    public EnergyContainersBuilder addBasic(Predicate<@NotNull AutomationType> canExtract,
                                            Predicate<@NotNull AutomationType> canInsert,
                                            IntSupplier maxEnergy, IntSupplier rate) {
        return addStorage((containerIndex, getter, setter, creator) ->
                new AttachedEnergyContainer(containerIndex, getter, setter, creator, canExtract, canInsert, maxEnergy, rate));
    }

    public EnergyContainersBuilder addStorage(IEnergyContainerCreator<? extends AttachedEnergyContainer> storage) {
        containerCreators.add(storage);
        return this;
    }

    private static class BaseEnergyContainerCreator extends ContainerCreator<Integer, AttachedEnergy, AttachedEnergyContainer> {
        public BaseEnergyContainerCreator(List<IBaseContainerCreator<Integer, AttachedEnergy, ? extends AttachedEnergyContainer>> creators) {
            super(creators);
        }

        @Override
        public AttachedEnergy initAttached(int containers) {
            return AttachedEnergy.create(containers);
        }
    }
}
