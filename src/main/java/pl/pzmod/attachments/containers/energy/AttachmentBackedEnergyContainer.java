package pl.pzmod.attachments.containers.energy;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.AttachmentBackedContainer;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.Action;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.capabilities.energy.IEnergyContainer;

import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AttachmentBackedEnergyContainer extends AttachmentBackedContainer<Integer, AttachedEnergy> implements IEnergyContainer {
    private final Predicate<@NotNull AutomationType> canInsert;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final IntSupplier maxEnergy;
    private final IntSupplier rate;

    public AttachmentBackedEnergyContainer(BlockEntity attachedTo,
                                           int containerIndex,
                                           Supplier<AttachedEnergy> newAttachmentCreator,
                                           Predicate<@NotNull AutomationType> canInsert,
                                           Predicate<@NotNull AutomationType> canExtract,
                                           IntSupplier maxEnergy,
                                           IntSupplier rate) {
        super(attachedTo, containerIndex, newAttachmentCreator);
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.maxEnergy = maxEnergy;
        this.rate = rate;
    }

    @Override
    protected @NotNull Integer copy(Integer toCopy) {
        return toCopy;
    }

    @Override
    protected @NotNull ContainerType<?, AttachedEnergy, ?> getContainerType() {
        return ContainerType.ENERGY;
    }

    @Override
    public int getMaxEnergy() {
        return maxEnergy.getAsInt();
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
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : rate.getAsInt();
    }

    protected int getExtractRate(@Nullable AutomationType automationType) {
        return automationType == null || automationType == AutomationType.MANUAL ? Integer.MAX_VALUE : rate.getAsInt();
    }

    @Override
    public int insert(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0 || !canInsert.test(automationType)) {
            return energy;
        }
        AttachedEnergy attachedEnergy = getAttached();
        int stored = getContents(attachedEnergy);
        int needed = Math.min(getInsertRate(automationType), getNeeded(stored));
        if (needed == 0) {
            return energy;
        }
        int toAdd = Math.min(energy, needed);
        if (action.execute()) {
            setContents(attachedEnergy, stored + toAdd);
        }
        return energy - toAdd;
    }

    @Override
    public int extract(int energy, @NotNull Action action, @NotNull AutomationType automationType) {
        if (energy <= 0) {
            return 0;
        }
        AttachedEnergy attachedEnergy = getAttached();
        int stored = getContents(attachedEnergy);
        if (stored == 0 || !canExtract.test(automationType)) {
            return 0;
        }
        int ret = Math.min(Math.min(getExtractRate(automationType), stored), energy);
        if (ret > 0 && action.execute()) {
            setContents(attachedEnergy, stored - ret);
        }
        return ret;
    }
}
