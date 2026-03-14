package pl.pzmod.data.containers;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.energy.AttachedEnergy;
import pl.pzmod.data.containers.fluids.AttachedFluids;
import pl.pzmod.data.containers.items.AttachedItems;
import pl.pzmod.registries.PZAttachments;
import pl.pzmod.registries.PZDataComponents;

import java.util.function.Supplier;

public class ContainerType<A extends IAttachedContainers<?, A>> {
    public static final ContainerType<AttachedEnergy> ENERGY =
            new ContainerType<>(AttachedEnergy.EMPTY, PZDataComponents.ENERGY_COMPONENT, PZAttachments.ENERGY_ATTACHMENT);

    public static final ContainerType<AttachedFluids> FLUIDS =
            new ContainerType<>(AttachedFluids.EMPTY, PZDataComponents.FLUIDS_COMPONENT, PZAttachments.FLUIDS_ATTACHMENT);

    public static final ContainerType<AttachedItems> ITEMS =
            new ContainerType<>(AttachedItems.EMPTY, PZDataComponents.ITEMS_COMPONENT, PZAttachments.ITEMS_ATTACHMENT);

    private final A emptyValue;
    private final Supplier<? extends DataComponentType<A>> dataComponent;
    private final Supplier<AttachmentType<A>> attachment;

    public ContainerType(A emptyValue,
                         Supplier<? extends DataComponentType<A>> dataComponent,
                         Supplier<AttachmentType<A>> attachment) {
        this.emptyValue = emptyValue;
        this.dataComponent = dataComponent;
        this.attachment = attachment;
    }

    public Supplier<? extends DataComponentType<A>> getDataComponent() {
        return dataComponent;
    }

    public Supplier<AttachmentType<A>> getAttachment() {
        return attachment;
    }

    public <H extends IContainerHolder> @NotNull A getOrEmpty(@NotNull H holder) {
        return holder.getContainers(this).orElse(emptyValue);
    }

    public <H extends IContainerHolder> void set(@NotNull H holder, @NotNull A value) {
        holder.setContainers(this, value);
    }

    public <H extends IContainerHolder> @NotNull A createNewAttachment(@NotNull H holder) {
        return emptyValue.withSize(holder.getContainerCount());
    }
}
