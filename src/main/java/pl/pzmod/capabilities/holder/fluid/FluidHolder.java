package pl.pzmod.capabilities.holder.fluid;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.fluid.AttachedFluids;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.attachments.containers.fluid.IFluidContainer;
import pl.pzmod.attachments.containers.fluid.IFluidContainerCreator;
import pl.pzmod.capabilities.holder.ContainersHolder;
import pl.pzmod.registries.PZAttachments;

import java.util.List;
import java.util.function.Supplier;

public class FluidHolder extends ContainersHolder<IFluidContainer> implements IFluidHolder {
    private final BlockEntity blockEntity;
    private int size;

    public FluidHolder(Supplier<Direction> facingSupplier, BlockEntity blockEntity) {
        super(facingSupplier);
        this.blockEntity = blockEntity;
    }

    void addFluidContainer(IFluidContainerCreator<? extends IFluidContainer> creator, RelativeSide... sides) {
        addContainerInternal(creator.create(size++, this::getAttached, this::setAttached, this::createAttached), sides);
    }

    private AttachedFluids getAttached() {
        return blockEntity.getExistingData(PZAttachments.FLUIDS_ATTACHMENT).orElse(AttachedFluids.EMPTY);
    }

    private void setAttached(AttachedFluids attachedFluids) {
        blockEntity.setData(PZAttachments.FLUIDS_ATTACHMENT, attachedFluids);
    }

    private AttachedFluids createAttached() {
        return AttachedFluids.create(size);
    }

    @Override
    public @NotNull List<IFluidContainer> getFluidContainers(@Nullable Direction side) {
        return getContainers(side);
    }
}
