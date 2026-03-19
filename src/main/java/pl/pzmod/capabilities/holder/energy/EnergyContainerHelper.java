package pl.pzmod.capabilities.holder.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.attachments.containers.creator.IAttachmentBackedContainerCreator;
import pl.pzmod.attachments.containers.energy.AttachedEnergy;
import pl.pzmod.attachments.containers.energy.AttachmentBackedEnergyContainer;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.energy.IEnergyContainer;

import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EnergyContainerHelper {
    public static EnergyContainerHelper forSide(Supplier<Direction> facingSupplier, @NotNull BlockEntity blockEntity) {
        return new EnergyContainerHelper(new EnergyHolder(facingSupplier), blockEntity);
    }

    private final EnergyHolder energyHolder;
    private final BlockEntity blockEntity;

    private int currentIndex;
    private boolean built;

    private EnergyContainerHelper(EnergyHolder energyHolder, BlockEntity blockEntity) {
        this.energyHolder = energyHolder;
        this.blockEntity = blockEntity;
        this.currentIndex = 0;
        this.built = false;
    }

    public IEnergyHolder build() {
        checkBuilt();
        built = true;
        return energyHolder;
    }

    public EnergyContainerHelper addBasic(IntSupplier rate, IntSupplier capacity, RelativeSide... sides) {
        return addStorage((type, attachedTo, containerIndex, newAttachmentCreator) ->
                new AttachmentBackedEnergyContainer(attachedTo, containerIndex, newAttachmentCreator,
                        AutomationType::manual, ConstantPredicates.alwaysTrue(), rate, capacity), sides);
    }

    public EnergyContainerHelper addBasic(Predicate<@NotNull AutomationType> canExtract,
                                          Predicate<@NotNull AutomationType> canInsert,
                                          IntSupplier rate,
                                          IntSupplier capacity,
                                          RelativeSide... sides) {
        return addStorage((type, attachedTo, containerIndex, newAttachmentCreator) ->
                new AttachmentBackedEnergyContainer(attachedTo, containerIndex, newAttachmentCreator, canExtract, canInsert, rate, capacity), sides);
    }

    public EnergyContainerHelper addStorage(@NotNull IAttachmentBackedContainerCreator<? extends AttachmentBackedEnergyContainer, AttachedEnergy> storage,
                                            RelativeSide... sides) {
        checkBuilt();
        var container = storage.create(ContainerType.ENERGY, blockEntity, currentIndex++, energyHolder::initAttachedEnergy);
        energyHolder.addEnergyContainer(container, sides);
        return this;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Holder already built.");
        }
    }
}
