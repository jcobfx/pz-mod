package pl.pzmod.capabilities.holder.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.data.containers.IAttachmentHolder;
import pl.pzmod.data.containers.energy.AttachedEnergy;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class EnergyContainerHelper {
    private final IMutableEnergyHolder containerHolder;
    private final IAttachmentHolder<AttachedEnergy> attachmentHolder;

    private int current;
    private boolean built;

    private EnergyContainerHelper(IMutableEnergyHolder containerHolder, IAttachmentHolder<AttachedEnergy> attachmentHolder) {
        this.containerHolder = containerHolder;
        this.attachmentHolder = attachmentHolder;
        current = 0;
        built = false;
    }

    public static EnergyContainerHelper forSide(Supplier<Direction> facingSupplier, IAttachmentHolder<AttachedEnergy> attachmentHolder) {
        return new EnergyContainerHelper(new EnergyHolder(facingSupplier), attachmentHolder);
    }

    public <C extends IEnergyContainer> C addContainer(BiFunction<IAttachmentHolder<AttachedEnergy>, Integer, C> containerFactory) {
        checkBuilt();
        var container = containerFactory.apply(attachmentHolder, current++);
        containerHolder.addEnergyContainer(container);
        return container;
    }

    public <C extends IEnergyContainer> C addContainer(BiFunction<IAttachmentHolder<AttachedEnergy>, Integer, C> containerFactory, RelativeSide... sides) {
        checkBuilt();
        var container = containerFactory.apply(attachmentHolder, current++);
        containerHolder.addEnergyContainer(container, sides);
        return container;
    }

    public IEnergyHolder build() {
        built = true;
        return containerHolder;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
    }
}
