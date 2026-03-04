package pl.pzmod.capabilities;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CapabilityResolver<H extends IDataHolder<?, A, T>, A, T, C> {
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

    protected A getData(A defaultValue) {
        return dataHolder.getData(dataType).orElse(defaultValue);
    }

    protected void setData(A value) {
        dataHolder.setData(dataType, value);
    }

    protected boolean canInsert() {
        return canInsert.test(context);
    }

    protected boolean canExtract() {
        return canExtract.test(context);
    }
}
