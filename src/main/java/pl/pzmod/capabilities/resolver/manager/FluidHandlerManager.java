package pl.pzmod.capabilities.resolver.manager;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.capabilities.holder.fluid.IFluidHolder;
import pl.pzmod.capabilities.proxy.FluidHandlerProxy;
import pl.pzmod.data.containers.fluids.ISidedFluidHandler;

public class FluidHandlerManager extends CapabilityHandlerManager<IFluidHolder, IFluidContainer, IFluidHandler, ISidedFluidHandler> {
    public FluidHandlerManager(IFluidHolder holder, ISidedFluidHandler baseHandler) {
        super(holder, baseHandler, FluidHandlerProxy::new, Capabilities.FLUID.block(), IFluidHolder::getFluidContainers);
    }
}
