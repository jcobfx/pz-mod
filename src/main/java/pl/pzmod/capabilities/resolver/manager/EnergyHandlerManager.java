package pl.pzmod.capabilities.resolver.manager;

import net.neoforged.neoforge.energy.IEnergyStorage;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.capabilities.holder.energy.IEnergyHolder;
import pl.pzmod.capabilities.proxy.EnergyHandlerProxy;
import pl.pzmod.data.containers.energy.ISidedEnergyHandler;

public class EnergyHandlerManager extends CapabilityHandlerManager<IEnergyHolder, IEnergyContainer, IEnergyStorage, ISidedEnergyHandler> {
    public EnergyHandlerManager(IEnergyHolder holder, ISidedEnergyHandler baseHandler) {
        super(holder, baseHandler, EnergyHandlerProxy::new, Capabilities.ENERGY.block(), IEnergyHolder::getEnergyContainers);
    }
}
