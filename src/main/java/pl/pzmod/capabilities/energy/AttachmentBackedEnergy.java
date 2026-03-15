package pl.pzmod.capabilities.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.AttachmentBackedContainer;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.capabilities.ContainerType;
import pl.pzmod.data.containers.IAttachmentHolder;
import pl.pzmod.data.containers.energy.AttachedEnergy;
import pl.pzmod.utils.ConstantPredicates;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class AttachmentBackedEnergy extends AttachmentBackedContainer<Integer, AttachedEnergy> implements IEnergyContainer {
    public static BiFunction<IAttachmentHolder<AttachedEnergy>, Integer, AttachmentBackedEnergy> inout(
            IntSupplier capacity,
            IntSupplier rate
    ) {
        return (holder, index) ->
                new AttachmentBackedEnergy(holder, index, ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), capacity, rate);
    }

    public static BiFunction<IAttachmentHolder<AttachedEnergy>, Integer, AttachmentBackedEnergy> input(
            IntSupplier capacity,
            IntSupplier rate
    ) {
        return (holder, index) ->
                new AttachmentBackedEnergy(holder, index, ConstantPredicates.alwaysTrue(), AutomationType::notExternal, capacity, rate);
    }

    public static BiFunction<IAttachmentHolder<AttachedEnergy>, Integer, AttachmentBackedEnergy> output(
            IntSupplier capacity,
            IntSupplier rate
    ) {
        return (holder, index) ->
                new AttachmentBackedEnergy(holder, index, AutomationType::notExternal, ConstantPredicates.alwaysTrue(), capacity, rate);
    }

    private final Predicate<@NotNull AutomationType> canInsert;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final IntSupplier capacity;
    private final IntSupplier rate;

    protected AttachmentBackedEnergy(IAttachmentHolder<AttachedEnergy> holder,
                                     int index,
                                     Predicate<@NotNull AutomationType> canInsert,
                                     Predicate<@NotNull AutomationType> canExtract,
                                     IntSupplier capacity,
                                     IntSupplier rate) {
        super(holder, index);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.capacity = capacity;
        this.rate = rate;
    }

    @Override
    protected @NotNull ContainerType<AttachedEnergy> getContainerType() {
        return ContainerType.ENERGY;
    }

    @Override
    public int getRate() {
        return rate.getAsInt();
    }

    @Override
    public int getCapacity() {
        return capacity.getAsInt();
    }

    @Override
    public int getEnergy() {
        return getContents(getAttached());
    }

    @Override
    public void setEnergy(int energy) {
        setContents(getAttached(), clampEnergy(energy));
    }

    @Override
    public boolean canInsert(@NotNull AutomationType automationType) {
        return canInsert.test(automationType);
    }

    @Override
    public boolean canExtract(@NotNull AutomationType automationType) {
        return canExtract.test(automationType);
    }
}
