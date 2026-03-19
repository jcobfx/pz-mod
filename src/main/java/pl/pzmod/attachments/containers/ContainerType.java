package pl.pzmod.attachments.containers;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.creator.IContainerCreator;
import pl.pzmod.attachments.containers.energy.AttachedEnergy;
import pl.pzmod.attachments.containers.energy.ComponentBackedEnergyHandler;
import pl.pzmod.attachments.containers.fluid.ComponentBackedFluidHandler;
import pl.pzmod.attachments.containers.fluid.AttachedFluids;
import pl.pzmod.attachments.containers.item.ComponentBackedItemHandler;
import pl.pzmod.attachments.containers.item.AttachedItems;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.ICapability;
import pl.pzmod.capabilities.energy.IEnergyContainer;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.capabilities.item.IItemContainer;
import pl.pzmod.registries.PZAttachments;
import pl.pzmod.registries.PZDataComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ContainerType<C, A extends IAttachedContainers<?, A>, H extends ComponentBackedHandler<C, ?, A>> {
    private static final List<ContainerType<?, ?, ?>> TYPES_INTERNAL = new ArrayList<>();
    public static final List<ContainerType<?, ?, ?>> TYPES = Collections.unmodifiableList(TYPES_INTERNAL);

    public static final ContainerType<IEnergyContainer, AttachedEnergy, ComponentBackedEnergyHandler> ENERGY =
            new ContainerType<>(ComponentBackedEnergyHandler::new, Capabilities.ENERGY, PZDataComponents.ENERGY_COMPONENT,
                    PZAttachments.ENERGY_ATTACHMENT, AttachedEnergy.EMPTY);

    public static final ContainerType<IFluidContainer, AttachedFluids, ComponentBackedFluidHandler> FLUIDS =
            new ContainerType<>(ComponentBackedFluidHandler::new, Capabilities.FLUID, PZDataComponents.FLUIDS_COMPONENT,
                    PZAttachments.FLUIDS_ATTACHMENT, AttachedFluids.EMPTY);

    public static final ContainerType<IItemContainer, AttachedItems, ComponentBackedItemHandler> ITEMS =
            new ContainerType<>(ComponentBackedItemHandler::new, Capabilities.ITEM, PZDataComponents.ITEMS_COMPONENT,
                    PZAttachments.ITEMS_ATTACHMENT, AttachedItems.EMPTY);

    public static boolean anySupports(Holder<Item> item) {
        for (ContainerType<?, ?, ?> type : TYPES) {
            if (type.knownDefaultCreators.containsKey(item.value())) {
                return true;
            }
        }
        return false;
    }

    private final Map<Item, Lazy<? extends IContainerCreator<? extends C, A>>> knownDefaultCreators;
    private final HandlerConstructor<H> handlerConstructor;
    private final ICapability<? super H, ?> capability;
    private final DeferredHolder<DataComponentType<?>, DataComponentType<A>> dataComponent;
    private final DeferredHolder<AttachmentType<?>, AttachmentType<A>> attachment;
    private final A emptyContainers;

    private ContainerType(HandlerConstructor<H> handlerConstructor,
                          ICapability<? super H, ?> capability,
                          DeferredHolder<DataComponentType<?>, DataComponentType<A>> dataComponent,
                          DeferredHolder<AttachmentType<?>, AttachmentType<A>> attachment,
                          A emptyContainers) {
        TYPES_INTERNAL.add(this);
        this.knownDefaultCreators = new Reference2ObjectOpenHashMap<>();
        this.handlerConstructor = handlerConstructor;
        this.capability = capability;
        this.dataComponent = dataComponent;
        this.attachment = attachment;
        this.emptyContainers = emptyContainers;
    }

    public DeferredHolder<DataComponentType<?>, DataComponentType<A>> getDataComponent() {
        return dataComponent;
    }

    public DeferredHolder<AttachmentType<?>, AttachmentType<A>> getAttachment() {
        return attachment;
    }

    public void addDefaultCreators(@Nullable IEventBus eventBus, @NotNull Item item, Supplier<? extends IContainerCreator<? extends C, A>> defaultCreator) {
        knownDefaultCreators.put(item, Lazy.of(defaultCreator));
        if (eventBus != null) {
            eventBus.addListener(RegisterCapabilitiesEvent.class, event -> registerItemCapabilities(event, item, false));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerItemCapabilities(RegisterCapabilitiesEvent event, Item item, boolean exposeWhenStacked) {
        event.registerItem((ItemCapability) capability.item(), getCapabilityProvider(exposeWhenStacked), item);
    }

    public int getContainerCount(@NotNull ItemStack attachedTo) {
        A attached = getOrEmpty(attachedTo);
        if (attached.isEmpty()) {
            Lazy<? extends IContainerCreator<? extends C, A>> containerCreator = knownDefaultCreators.get(attachedTo.getItem());
            return containerCreator == null ? 0 : containerCreator.get().totalContainers();
        }
        return attached.size();
    }

    public @Nullable H createHandler(@NotNull ItemStack attachedTo) {
        int count = getContainerCount(attachedTo);
        if (count == 0) {
            return null;
        }
        return handlerConstructor.create(attachedTo, count);
    }

    public @NotNull C createContainer(@NotNull ItemStack attachedTo, int containerIndex) {
        Lazy<? extends IContainerCreator<? extends C, A>> lazy = knownDefaultCreators.get(attachedTo.getItem());
        if (lazy != null) {
            IContainerCreator<? extends C, A> containerCreator = lazy.get();
            return containerCreator.create(this, attachedTo, containerIndex);
        }
        throw new IllegalStateException("No container creator found for " + attachedTo.getItem() + " and container type " + this);
    }

    public @NotNull A createNewAttachment(@NotNull ItemStack attachedTo) {
        Lazy<? extends IContainerCreator<? extends C, A>> lazy = knownDefaultCreators.get(attachedTo.getItem());
        if (lazy == null) {
            return emptyContainers;
        }
        IContainerCreator<? extends C, A> containerCreator = lazy.get();
        int count = containerCreator.totalContainers();
        if (count == 0) {
            return emptyContainers;
        }
        return containerCreator.initAttached(count);
    }

    public @NotNull A getOrEmpty(@NotNull ItemStack attachedTo) {
        return attachedTo.getOrDefault(dataComponent, emptyContainers);
    }

    public @NotNull A getOrEmpty(@NotNull BlockEntity attachedTo) {
        return attachedTo.getExistingData(attachment).orElse(emptyContainers);
    }

    public void set(@NotNull ItemStack attachedTo, @NotNull A containers) {
        attachedTo.set(dataComponent, containers);
    }

    public void set(@NotNull BlockEntity attachedTo, @NotNull A containers) {
        attachedTo.setData(attachment, containers);
    }

    public boolean supports(ItemStack stack) {
        return stack.has(dataComponent) || knownDefaultCreators.containsKey(stack.getItem());
    }

    public void addDefault(Holder<Item> item, DataComponentPatch.Builder builder) {
        Lazy<? extends IContainerCreator<? extends C, A>> lazy = knownDefaultCreators.get(item.value());
        if (lazy != null) {
            IContainerCreator<? extends C, A> containerCreator = lazy.get();
            int count = containerCreator.totalContainers();
            if (count > 0) {
                builder.set(dataComponent.get(), containerCreator.initAttached(count));
            }
        }
    }

    private ICapabilityProvider<ItemStack, Void, ? super H> getCapabilityProvider(boolean exposeWhenStacked) {
        if (exposeWhenStacked) {
            return (stack, context) -> createHandler(stack);
        }
        return (stack, context) -> stack.getCount() == 1 ? createHandler(stack) : null;
    }

    @FunctionalInterface
    private interface HandlerConstructor<H extends ComponentBackedHandler<?, ?, ?>> {
        H create(ItemStack attachedTo, int totalContainers);
    }
}
