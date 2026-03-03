package pl.pzmod.capabilities;

import pl.pzmod.capabilities.proxy.Proxy;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CapabilityResolver<H extends Proxy<?>, T, C> {
    private final H dataHolder;
    private final Supplier<T> dataType;
    private final C context;
    private final Predicate<C> canInsert;
    private final Predicate<C> canExtract;

    protected CapabilityResolver(H dataHolder,
                                 Supplier<T> dataType,
                                 C context,
                                 Predicate<C> canInsert,
                                 Predicate<C> canExtract) {
        this.dataHolder = dataHolder;
        this.dataType = dataType;
        this.context = context;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    protected H getDataHolder() {
        return dataHolder;
    }

    protected <D> D getData(D defaultValue) {
        return dataHolder.getData(dataType, defaultValue);
    }

    protected <D> void setData(D value) {
        dataHolder.setData(dataType, value);
    }

    protected boolean canInsert() {
        return canInsert.test(context);
    }

    protected boolean canExtract() {
        return canExtract.test(context);
    }
}
