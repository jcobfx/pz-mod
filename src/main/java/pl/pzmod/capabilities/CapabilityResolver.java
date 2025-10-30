package pl.pzmod.capabilities;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.common.MutableDataComponentHolder;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CapabilityResolver<CONTEXT, ATTACHED> {
    private final MutableDataComponentHolder parent;
    private final DataComponentType<ATTACHED> component;
    private final CONTEXT context;
    private final Predicate<CONTEXT> canInsert;
    private final Predicate<CONTEXT> canExtract;

    protected CapabilityResolver(MutableDataComponentHolder parent,
                                 Supplier<DataComponentType<ATTACHED>> component,
                                 CONTEXT context,
                                 Predicate<CONTEXT> canInsert,
                                 Predicate<CONTEXT> canExtract) {
        this.parent = parent;
        this.component = component.get();
        this.context = context;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
    }

    protected ATTACHED getAttached(ATTACHED defaultValue) {
        return parent.getOrDefault(component, defaultValue);
    }

    protected void setAttached(ATTACHED value) {
        parent.set(component, value);
    }

    protected CONTEXT getContext() {
        return context;
    }

    protected boolean canInsert() {
        return canInsert.test(context);
    }

    protected boolean canExtract() {
        return canExtract.test(context);
    }
}
