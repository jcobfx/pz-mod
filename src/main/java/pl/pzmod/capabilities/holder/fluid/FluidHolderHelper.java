package pl.pzmod.capabilities.holder.fluid;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.attachments.containers.fluid.AttachedFluidContainer;
import pl.pzmod.attachments.containers.fluid.IFluidContainerCreator;

import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FluidHolderHelper {
    public static FluidHolderHelper forSide(Supplier<Direction> facingSupplier, @NotNull BlockEntity blockEntity) {
        return new FluidHolderHelper(new FluidHolder(facingSupplier, blockEntity));
    }

    private final FluidHolder fluidHolder;

    private boolean built;

    private FluidHolderHelper(FluidHolder fluidHolder) {
        this.fluidHolder = fluidHolder;
        this.built = false;
    }

    public IFluidHolder build() {
        checkBuilt();
        built = true;
        return fluidHolder;
    }

    public FluidHolderHelper addBasic(int capacity, int rate, RelativeSide... sides) {
        return addBasic(() -> capacity, () -> rate, sides);
    }

    public FluidHolderHelper addBasic(IntSupplier capacity, IntSupplier rate, RelativeSide... sides) {
        return addTank((containerIndex, getter, setter, creator) ->
                new AttachedFluidContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.manualOnly(), capacity, rate), sides);
    }

    public FluidHolderHelper addBasicExtractable(Predicate<@NotNull FluidStack> isFluidValid, int capacity, int rate, RelativeSide... sides) {
        return addBasicExtractable(isFluidValid, () -> capacity, () -> rate, sides);
    }

    public FluidHolderHelper addBasicExtractable(Predicate<@NotNull FluidStack> isFluidValid, IntSupplier capacity, IntSupplier rate, RelativeSide... sides) {
        return addTank((containerIndex, getter, setter, creator) ->
                new AttachedFluidContainer(containerIndex, getter, setter, creator, isFluidValid,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi(), capacity, rate), sides);
    }

    public FluidHolderHelper addTank(@NotNull IFluidContainerCreator<? extends AttachedFluidContainer> tank, RelativeSide... sides) {
        checkBuilt();
        fluidHolder.addFluidContainer(tank, sides);
        return this;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Holder already built.");
        }
    }

}
