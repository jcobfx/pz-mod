package pl.pzmod.attachments.containers.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.ComponentBackedHandler;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.capabilities.energy.IPZEnergyHandler;

import java.util.List;
import java.util.Optional;

public class ComponentBackedEnergyHandler extends ComponentBackedHandler<IEnergyContainer, Integer, AttachedEnergy> implements IPZEnergyHandler {
    public ComponentBackedEnergyHandler(ItemStack attachedTo, int totalContainers) {
        super(attachedTo, totalContainers);
    }

    @Override
    protected ContainerType<IEnergyContainer, AttachedEnergy, ?> containerType() {
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

    @Override
    public int getStorages(@Nullable Direction side) {
        return totalContainers();
    }

    @Override
    public int getEnergyInStorage(int storage, @Nullable Direction side) {
        return getContents(storage);
    }
}
