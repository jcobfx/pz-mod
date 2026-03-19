package pl.pzmod.capabilities.holder.fluid;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.attachments.containers.creator.IAttachmentBackedContainerCreator;
import pl.pzmod.attachments.containers.fluid.AttachedFluids;
import pl.pzmod.attachments.containers.fluid.AttachmentBackedFluidContainer;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.fluid.IFluidContainer;

import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FluidContainerHelper {
    public static FluidContainerHelper forSide(Supplier<Direction> facingSupplier, @NotNull BlockEntity blockEntity) {
        return new FluidContainerHelper(new FluidHolder(facingSupplier), blockEntity);
    }

    private final FluidHolder fluidHolder;
    private final BlockEntity blockEntity;

    private int currentIndex;
    private boolean built;

    private FluidContainerHelper(FluidHolder fluidHolder, BlockEntity blockEntity) {
        this.fluidHolder = fluidHolder;
        this.blockEntity = blockEntity;
        this.currentIndex = 0;
        this.built = false;
    }

    public IFluidHolder build() {
        checkBuilt();
        built = true;
        return fluidHolder;
    }

    public FluidContainerHelper addBasic(Predicate<@NotNull FluidStack> isValid, int capacity, int rate, RelativeSide... sides) {
        return addBasic(isValid, () -> capacity, () -> rate, sides);
    }

    public FluidContainerHelper addBasic(Predicate<@NotNull FluidStack> isValid, IntSupplier capacity, IntSupplier rate, RelativeSide... sides) {
        return addTank((type, attachedTo, containerIndex, newAttachmentCreator) ->
                new AttachmentBackedFluidContainer(attachedTo, containerIndex, newAttachmentCreator,
                        ConstantPredicates.manualOnly(), ConstantPredicates.alwaysTrueBi(), isValid, capacity, rate), sides);
    }

    public FluidContainerHelper addBasic(int capacity, int rate, RelativeSide... sides) {
        return addBasic(() -> capacity, () -> rate, sides);
    }

    public FluidContainerHelper addBasic(IntSupplier capacity, IntSupplier rate, RelativeSide... sides) {
        return addTank((type, attachedTo, containerIndex, newAttachmentCreator) ->
                new AttachmentBackedFluidContainer(attachedTo, containerIndex, newAttachmentCreator,
                        ConstantPredicates.manualOnly(), ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrue(), capacity, rate), sides);
    }

    public FluidContainerHelper addBasicExtractable(Predicate<@NotNull FluidStack> validator, IntSupplier capacity, IntSupplier rate, RelativeSide... sides) {
        return addTank((type, attachedTo, containerIndex, newAttachmentCreator) ->
                new AttachmentBackedFluidContainer(attachedTo, containerIndex, newAttachmentCreator,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi(), validator, capacity, rate), sides);
    }

    public FluidContainerHelper addTank(@NotNull IAttachmentBackedContainerCreator<? extends AttachmentBackedFluidContainer, AttachedFluids> tank,
                                        RelativeSide... sides) {
        checkBuilt();
        var container = tank.create(ContainerType.FLUIDS, blockEntity, currentIndex++, fluidHolder::initAttachedFluids);
        fluidHolder.addFluidContainer(container, sides);
        return this;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Holder already built.");
        }
    }
}
