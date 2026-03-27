package pl.pzmod.attachments.containers.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import pl.pzmod.attachments.containers.creator.IBaseContainerCreator;

public interface IFluidContainerCreator<C extends AttachedFluidContainer> extends IBaseContainerCreator<FluidStack, AttachedFluids, C> {

}
