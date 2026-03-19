package pl.pzmod.attachments.containers.fluid;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.ComponentBackedHandler;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.capabilities.fluid.IPZFluidHandler;

import java.util.List;
import java.util.Optional;

public class ComponentBackedFluidHandler extends ComponentBackedHandler<IFluidContainer, FluidStack, AttachedFluids> implements IPZFluidHandler {
    public ComponentBackedFluidHandler(ItemStack attachedTo, int totalContainers) {
        super(attachedTo, totalContainers);
    }

    @Override
    protected ContainerType<IFluidContainer, AttachedFluids, ?> containerType() {
        return ContainerType.FLUIDS;
    }

    @Override
    public @NotNull List<IFluidContainer> getFluidContainers(@Nullable Direction side) {
        return getContainers();
    }

    @Override
    public Optional<IFluidContainer> getFluidContainer(int tank, @Nullable Direction side) {
        return Optional.of(getContainer(tank));
    }

    @Override
    public int getTanks(@Nullable Direction side) {
        return totalContainers();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank, @Nullable Direction side) {
        return getContents(tank);
    }
}
