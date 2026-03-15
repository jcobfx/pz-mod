package pl.pzmod.capabilities;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.attachment.AttachmentType;
import pl.pzmod.data.containers.IAttachedContainers;
import pl.pzmod.data.containers.energy.AttachedEnergy;
import pl.pzmod.data.containers.fluids.AttachedFluids;
import pl.pzmod.data.containers.items.AttachedItems;
import pl.pzmod.registries.PZAttachments;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Supplier;

public record ContainerType<A extends IAttachedContainers<?, A>>(A emptyValue,
                                                                 Supplier<? extends DataComponentType<A>> dataComponent,
                                                                 Supplier<AttachmentType<A>> attachment) {
    public static final ContainerType<AttachedEnergy> ENERGY =
            new ContainerType<>(AttachedEnergy.EMPTY, PZDataComponents.ENERGY_COMPONENT, PZAttachments.ENERGY_ATTACHMENT);

    public static final ContainerType<AttachedFluids> FLUIDS =
            new ContainerType<>(AttachedFluids.EMPTY, PZDataComponents.FLUIDS_COMPONENT, PZAttachments.FLUIDS_ATTACHMENT);

    public static final ContainerType<AttachedItems> ITEMS =
            new ContainerType<>(AttachedItems.EMPTY, PZDataComponents.ITEMS_COMPONENT, PZAttachments.ITEMS_ATTACHMENT);
}
