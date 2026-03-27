package pl.pzmod.attachments.containers.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.AttachedHandler;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.energy.IPZEnergyHandler;

import java.util.List;
import java.util.Optional;

public class AttachedEnergyHandler extends AttachedHandler<AttachedEnergy, IEnergyContainer> implements IPZEnergyHandler {
    public AttachedEnergyHandler(ItemStack attachedTo, int totalContainers) {
        super(attachedTo, totalContainers);
    }

    @Override
    protected @NotNull ContainerType<IEnergyContainer, AttachedEnergy, ?> containerType() {
        return ContainerType.ENERGY;
    }

    @Override
    public @NotNull List<IEnergyContainer> getEnergyContainers(@Nullable Direction side) {
        return getContainers();
    }

    @Override
    public Optional<IEnergyContainer> getEnergyContainer(int storage, @Nullable Direction side) {
        return Optional.of(getContainer(storage));
    }
}
