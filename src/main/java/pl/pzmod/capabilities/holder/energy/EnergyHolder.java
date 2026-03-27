package pl.pzmod.capabilities.holder.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.energy.AttachedEnergy;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.attachments.containers.energy.IEnergyContainer;
import pl.pzmod.attachments.containers.energy.IEnergyContainerCreator;
import pl.pzmod.capabilities.holder.ContainersHolder;
import pl.pzmod.registries.PZAttachments;

import java.util.List;
import java.util.function.Supplier;

public class EnergyHolder extends ContainersHolder<IEnergyContainer> implements IEnergyHolder {
    private final BlockEntity blockEntity;
    private int size;

    public EnergyHolder(Supplier<Direction> facingSupplier, BlockEntity blockEntity) {
        super(facingSupplier);
        this.blockEntity = blockEntity;
    }

    void addEnergyContainer(IEnergyContainerCreator<? extends IEnergyContainer> creator, RelativeSide... sides) {
        addContainerInternal(creator.create(size++, this::getAttached, this::setAttached, this::createAttached), sides);
    }

    private AttachedEnergy getAttached() {
        return blockEntity.getExistingData(PZAttachments.ENERGY_ATTACHMENT).orElse(AttachedEnergy.EMPTY);
    }

    private void setAttached(AttachedEnergy attachedEnergy) {
        blockEntity.setData(PZAttachments.ENERGY_ATTACHMENT, attachedEnergy);
    }

    private AttachedEnergy createAttached() {
        return AttachedEnergy.create(size);
    }

    @Override
    public @NotNull List<IEnergyContainer> getEnergyContainers(@Nullable Direction side) {
        return getContainers(side);
    }
}
