package pl.pzmod.attachments.containers.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.creator.ContainerCreator;
import pl.pzmod.attachments.containers.creator.IBasicContainerCreator;
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

    private final List<IBasicContainerCreator<? extends ComponentBackedEnergyContainer>> containerCreators;

    private EnergyContainersBuilder() {
        this.containerCreators = new ArrayList<>();
    }

    public ContainerCreator<ComponentBackedEnergyContainer, AttachedEnergy> build() {
        return new BaseEnergyContainerCreator(containerCreators);
    }

    public EnergyContainersBuilder addBasic(IntSupplier rate, IntSupplier capacity) {
        return addStorage((type, attachedTo, containerIndex) ->
                new ComponentBackedEnergyContainer(attachedTo, containerIndex, AutomationType::manual, ConstantPredicates.alwaysTrue(), rate, capacity));
    }

    public EnergyContainersBuilder addBasic(Predicate<@NotNull AutomationType> canExtract,
                                            Predicate<@NotNull AutomationType> canInsert,
                                            IntSupplier rate,
                                            IntSupplier capacity) {
        return addStorage((type, attachedTo, containerIndex) ->
                new ComponentBackedEnergyContainer(attachedTo, containerIndex, canExtract, canInsert, rate, capacity));
    }

    public EnergyContainersBuilder addStorage(IBasicContainerCreator<? extends ComponentBackedEnergyContainer> storage) {
        containerCreators.add(storage);
        return this;
    }

    private static class BaseEnergyContainerCreator extends ContainerCreator<ComponentBackedEnergyContainer, AttachedEnergy> {
        public BaseEnergyContainerCreator(List<IBasicContainerCreator<? extends ComponentBackedEnergyContainer>> creators) {
            super(creators);
        }

        @Override
        public AttachedEnergy initAttached(int containers) {
            return AttachedEnergy.create(containers);
        }
    }
}
