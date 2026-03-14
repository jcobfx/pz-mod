package pl.pzmod.data.containers.energy;

import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.ContainerType;
import pl.pzmod.data.containers.BaseContainer;
import pl.pzmod.data.containers.IContainerHolder;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class EnergyContainer extends BaseContainer<Integer, AttachedEnergy> implements IEnergyContainer {
    private final Predicate<@NotNull AutomationType> canInsert;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final IntSupplier capacity;
    private final IntSupplier rate;

    public EnergyContainer(IContainerHolder holder,
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
    public @NotNull ContainerType<AttachedEnergy> getContainerType() {
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
