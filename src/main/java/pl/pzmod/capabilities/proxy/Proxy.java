package pl.pzmod.capabilities.proxy;

import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.fluid.IFluidHolder;
import pl.pzmod.capabilities.item.IItemHolder;

import java.util.function.Supplier;

public abstract class Proxy<P> implements IEnergyHolder, IItemHolder, IFluidHolder {
    private final P parent;

    protected Proxy(P parent) {
        this.parent = parent;
    }

    public P getParent() {
        return parent;
    }

    public abstract <D, T> D getData(Supplier<? extends T> dataType, D defaultValue);

    public abstract <D, T> void setData(Supplier<? extends T> dataType, D value);
}
