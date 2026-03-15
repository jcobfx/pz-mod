package pl.pzmod.capabilities.holder.fluid;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.data.containers.IAttachmentHolder;
import pl.pzmod.data.containers.fluids.AttachedFluids;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class FluidContainerHelper {
    private final IMutableFluidHolder containerHolder;
    private final IAttachmentHolder<AttachedFluids> attachmentHolder;

    private int current;
    private boolean built;

    private FluidContainerHelper(IMutableFluidHolder containerHolder, IAttachmentHolder<AttachedFluids> attachmentHolder) {
        this.containerHolder = containerHolder;
        this.attachmentHolder = attachmentHolder;
        this.current = 0;
        this.built = false;
    }

    public static FluidContainerHelper forSide(Supplier<Direction> facingSupplier, IAttachmentHolder<AttachedFluids> attachmentHolder) {
        return new FluidContainerHelper(new FluidHolder(facingSupplier), attachmentHolder);
    }

    public <C extends IFluidContainer> C addContainer(BiFunction<IAttachmentHolder<AttachedFluids>, Integer, C> containerFactory) {
        checkBuilt();
        var container = containerFactory.apply(attachmentHolder, current++);
        containerHolder.addFluidContainer(container);
        return container;
    }

    public <C extends IFluidContainer> C addContainer(BiFunction<IAttachmentHolder<AttachedFluids>, Integer, C> containerFactory, RelativeSide... sides) {
        checkBuilt();
        var container = containerFactory.apply(attachmentHolder, current++);
        containerHolder.addFluidContainer(container, sides);
        return container;
    }

    public IFluidHolder build() {
        built = true;
        return containerHolder;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
    }
}
