package pl.pzmod.attachments.containers.energy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.attachments.containers.AttachedContainer;

import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AttachedEnergyContainer extends AttachedContainer<Integer, AttachedEnergy> implements IEnergyContainer {
    private final Predicate<@NotNull AutomationType> canInsert;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final IntSupplier maxEnergy;
    private final IntSupplier rate;

    public AttachedEnergyContainer(int index,
                                   Supplier<@NotNull AttachedEnergy> attachedGetter,
                                   Consumer<@NotNull AttachedEnergy> attachedSetter,
                                   Supplier<@NotNull AttachedEnergy> attachedCreator,
                                   Predicate<@NotNull AutomationType> canInsert,
                                   Predicate<@NotNull AutomationType> canExtract,
                                   IntSupplier maxEnergy,
                                   IntSupplier rate) {
        super(index, attachedGetter, attachedSetter, attachedCreator);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.maxEnergy = maxEnergy;
        this.rate = rate;
    }

    @Override
    public int getMaxEnergy() {
        return maxEnergy.getAsInt();
    }

    @Override
    public int getRate() {
        return rate.getAsInt();
    }

    @Override
    public boolean canInsert(@NotNull AutomationType automationType) {
        return canInsert.test(automationType);
    }

    @Override
    public boolean canExtract(@NotNull AutomationType automationType) {
        return canExtract.test(automationType);
    }

    @Override
    public int getEnergy() {
        return getContents(getAttached());
    }

    @Override
    public void setEnergy(int energy) {
        setContents(getAttached(), clampEnergy(energy));
    }

    protected int getNeeded(int energy) {
        return Math.max(0, getMaxEnergy() - energy);
    }

    protected int getInsertRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : getRate();
    }

    protected int getExtractRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : getRate();
    }

    @Override
    public int insert(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0 || !canInsert(automationType)) {
            return energy;
        }
        AttachedEnergy attachedEnergy = getAttached();
        int stored = getContents(attachedEnergy);
        int needed = Math.min(getInsertRate(automationType), getNeeded(stored));
        if (needed == 0) {
            return energy;
        }
        int toInsert = Math.min(energy, needed);
        if (action.execute()) {
            setContents(attachedEnergy, stored + toInsert);
        }
        return energy - toInsert;
    }

    @Override
    public int extract(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0) {
            return 0;
        }
        AttachedEnergy attachedEnergy = getAttached();
        int stored = getContents(attachedEnergy);
        if (stored == 0 || !canExtract(automationType)) {
            return 0;
        }
        int toExtract = Math.min(Math.min(getExtractRate(automationType), stored), energy);
        if (toExtract > 0 && action.execute()) {
            setContents(attachedEnergy, stored - toExtract);
        }
        return toExtract;
    }
}
