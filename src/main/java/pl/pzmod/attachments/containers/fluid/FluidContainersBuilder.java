package pl.pzmod.attachments.containers.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.creator.ContainerCreator;
import pl.pzmod.attachments.containers.creator.IBasicContainerCreator;
import pl.pzmod.attachments.containers.ConstantPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class FluidContainersBuilder {
    public static FluidContainersBuilder builder() {
        return new FluidContainersBuilder();
    }

    private final List<IBasicContainerCreator<? extends ComponentBackedFluidContainer>> tankCreators;

    private FluidContainersBuilder() {
        this.tankCreators = new ArrayList<>();
    }

    public ContainerCreator<ComponentBackedFluidContainer, AttachedFluids> build() {
        return new BaseFluidTankCreator(tankCreators);
    }

    public FluidContainersBuilder addBasic(Predicate<@NotNull FluidStack> isValid, int capacity, int rate) {
        return addBasic(isValid, () -> capacity, () -> rate);
    }

    public FluidContainersBuilder addBasic(Predicate<@NotNull FluidStack> isValid, IntSupplier capacity, IntSupplier rate) {
        return addTank((type, attachedTo, containerIndex) ->
                new ComponentBackedFluidContainer(attachedTo, containerIndex, ConstantPredicates.alwaysTrueBi(), ConstantPredicates.manualOnly(),
                        isValid, capacity, rate));
    }

    public FluidContainersBuilder addBasic(int capacity, int rate) {
        return addBasic(() -> capacity, () -> rate);
    }

    public FluidContainersBuilder addBasic(IntSupplier capacity, IntSupplier rate) {
        return addTank((type, attachedTo, containerIndex) ->
                new ComponentBackedFluidContainer(attachedTo, containerIndex, ConstantPredicates.alwaysTrueBi(), ConstantPredicates.manualOnly(),
                        ConstantPredicates.alwaysTrue(), capacity, rate));
    }

    public FluidContainersBuilder addBasicExtractable(Predicate<@NotNull FluidStack> validator, IntSupplier capacity, IntSupplier rate) {
        return addTank((type, attachedTo, containerIndex) ->
                new ComponentBackedFluidContainer(attachedTo, containerIndex, ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi(),
                        validator, capacity, rate));
    }

    public FluidContainersBuilder addTank(IBasicContainerCreator<? extends ComponentBackedFluidContainer> tank) {
        tankCreators.add(tank);
        return this;
    }

    private static class BaseFluidTankCreator extends ContainerCreator<ComponentBackedFluidContainer, AttachedFluids> {
        public BaseFluidTankCreator(List<IBasicContainerCreator<? extends ComponentBackedFluidContainer>> creators) {
            super(creators);
        }

        @Override
        public AttachedFluids initAttached(int containers) {
            return AttachedFluids.create(containers);
        }
    }
}
