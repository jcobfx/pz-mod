package pl.pzmod.attachments.containers.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.creator.ContainerCreator;
import pl.pzmod.attachments.containers.creator.IBaseContainerCreator;
import pl.pzmod.attachments.containers.ConstantPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class FluidContainersBuilder {
    public static FluidContainersBuilder builder() {
        return new FluidContainersBuilder();
    }

    private final List<IBaseContainerCreator<FluidStack, AttachedFluids, ? extends AttachedFluidContainer>> tankCreators;

    private FluidContainersBuilder() {
        this.tankCreators = new ArrayList<>();
    }

    public ContainerCreator<FluidStack, AttachedFluids, AttachedFluidContainer> build() {
        return new BaseFluidTankCreator(tankCreators);
    }

    public FluidContainersBuilder addBasic(Predicate<@NotNull FluidStack> isValid, int capacity, int rate) {
        return addBasic(isValid, () -> capacity, () -> rate);
    }

    public FluidContainersBuilder addBasic(Predicate<@NotNull FluidStack> isValid, IntSupplier capacity, IntSupplier rate) {
        return addTank((containerIndex, getter, setter, creator) ->
                new AttachedFluidContainer(containerIndex, getter, setter, creator, isValid,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.manualOnly(), capacity, rate));
    }

    public FluidContainersBuilder addBasic(int capacity, int rate) {
        return addBasic(() -> capacity, () -> rate);
    }

    public FluidContainersBuilder addBasic(IntSupplier capacity, IntSupplier rate) {
        return addTank((containerIndex, getter, setter, creator) ->
                new AttachedFluidContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.manualOnly(), capacity, rate));
    }

    public FluidContainersBuilder addBasicExtractable(Predicate<@NotNull FluidStack> isValid, IntSupplier capacity, IntSupplier rate) {
        return addTank((containerIndex, getter, setter, creator) ->
                new AttachedFluidContainer(containerIndex, getter, setter, creator, isValid,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi(), capacity, rate));
    }

    public FluidContainersBuilder addTank(IFluidContainerCreator<? extends AttachedFluidContainer> tank) {
        tankCreators.add(tank);
        return this;
    }

    private static class BaseFluidTankCreator extends ContainerCreator<FluidStack, AttachedFluids, AttachedFluidContainer> {
        public BaseFluidTankCreator(List<IBaseContainerCreator<FluidStack, AttachedFluids, ? extends AttachedFluidContainer>> creators) {
            super(creators);
        }

        @Override
        public AttachedFluids initAttached(int containers) {
            return AttachedFluids.create(containers);
        }
    }
}
