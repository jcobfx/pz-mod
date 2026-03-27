package pl.pzmod.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.attachments.containers.energy.EnergyContainersBuilder;
import pl.pzmod.attachments.containers.fluid.FluidContainersBuilder;
import pl.pzmod.attachments.containers.item.ItemContainersBuilder;

public abstract class PZItem extends Item implements IContainerItem {
    protected PZItem(Properties properties) {
        super(properties);
    }

    protected @NotNull EnergyContainersBuilder addDefaultEnergyContainers(@NotNull EnergyContainersBuilder builder) {
        return builder;
    }

    public boolean hasEnergyContainers() {
        return false;
    }

    protected @NotNull FluidContainersBuilder addDefaultFluidContainers(@NotNull FluidContainersBuilder builder) {
        return builder;
    }

    public boolean hasFluidContainers() {
        return false;
    }

    protected @NotNull ItemContainersBuilder addDefaultItemContainers(@NotNull ItemContainersBuilder builder) {
        return builder;
    }

    public boolean hasItemContainers() {
        return false;
    }

    @Override
    public void attachDefaultContainers(@NotNull IEventBus bus) {
        if (hasEnergyContainers()) {
            ContainerType.ENERGY.addDefaultCreators(bus, this, addDefaultEnergyContainers(EnergyContainersBuilder.builder()).build());
        }
        if (hasFluidContainers()) {
            ContainerType.FLUID.addDefaultCreators(bus, this, addDefaultFluidContainers(FluidContainersBuilder.builder()).build());
        }
        if (hasItemContainers()) {
            ContainerType.ITEM.addDefaultCreators(bus, this, addDefaultItemContainers(ItemContainersBuilder.builder()).build());
        }
    }
}
