package pl.pzmod.attachments.containers.fluid;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.AttachedHandler;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.fluid.IPZFluidHandler;

import java.util.List;
import java.util.Optional;

public class AttachedFluidHandler extends AttachedHandler<AttachedFluids, IFluidContainer> implements IPZFluidHandler {
    public AttachedFluidHandler(ItemStack attachedTo, int totalContainers) {
        super(attachedTo, totalContainers);
    }

    @Override
    protected @NotNull ContainerType<IFluidContainer, AttachedFluids, ?> containerType() {
        return ContainerType.FLUID;
    }

    @Override
    public @NotNull List<IFluidContainer> getFluidContainers(@Nullable Direction side) {
        return getContainers();
    }

    @Override
    public Optional<IFluidContainer> getFluidContainer(int tank, @Nullable Direction side) {
        return Optional.of(getContainer(tank));
    }
}
