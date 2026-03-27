package pl.pzmod.attachments.containers;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.attachments.containers.creator.IContainerCreator;
import pl.pzmod.attachments.containers.energy.AttachedEnergy;
import pl.pzmod.attachments.containers.fluid.AttachedFluids;
import pl.pzmod.attachments.containers.item.AttachedItems;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.ICapability;
import pl.pzmod.attachments.containers.energy.AttachedEnergyHandler;
import pl.pzmod.attachments.containers.energy.IEnergyContainer;
import pl.pzmod.attachments.containers.fluid.AttachedFluidHandler;
import pl.pzmod.attachments.containers.fluid.IFluidContainer;
import pl.pzmod.attachments.containers.item.AttachedItemHandler;
import pl.pzmod.attachments.containers.item.IItemContainer;
import pl.pzmod.registries.PZDataComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ContainerType<C, A extends IAttachedContainers<?, A>, H extends AttachedHandler<A, C>> {
    private static final List<ContainerType<?, ?, ?>> TYPES_INTERNAL = new ArrayList<>();
    public static final List<ContainerType<?, ?, ?>> TYPES = Collections.unmodifiableList(TYPES_INTERNAL);

    public static final ContainerType<IEnergyContainer, AttachedEnergy, AttachedEnergyHandler> ENERGY =
            new ContainerType<>(PZDataComponents.ENERGY_COMPONENT, Capabilities.ENERGY, AttachedEnergy.EMPTY, AttachedEnergyHandler::new);
    public static final ContainerType<IFluidContainer, AttachedFluids, AttachedFluidHandler> FLUID =
            new ContainerType<>(PZDataComponents.FLUIDS_COMPONENT, Capabilities.FLUID, AttachedFluids.EMPTY, AttachedFluidHandler::new);
    public static final ContainerType<IItemContainer, AttachedItems, AttachedItemHandler> ITEM =
            new ContainerType<>(PZDataComponents.ITEMS_COMPONENT, Capabilities.ITEM, AttachedItems.EMPTY, AttachedItemHandler::new);

    public static boolean anySupports(Holder<Item> item) {
        for (ContainerType<?, ?, ?> type : TYPES) {
            if (type.defaultCreators.containsKey(item.value())) {
                return true;
            }
        }
        return false;
    }

    private final DeferredHolder<DataComponentType<?>, DataComponentType<A>> component;
    private final ICapability<? super H, ?> capability;
    private final A emptyAttachment;
    private final BiFunction<ItemStack, Integer, H> handlerConstructor;

    private final Map<Item, Lazy<? extends IContainerCreator<?, A, ? extends C>>> defaultCreators;

    public ContainerType(DeferredHolder<DataComponentType<?>, DataComponentType<A>> component,
                         ICapability<? super H, ?> capability,
                         A emptyAttachment,
                         BiFunction<ItemStack, Integer, H> handlerConstructor) {
        TYPES_INTERNAL.add(this);
        this.component = component;
        this.capability = capability;
        this.emptyAttachment = emptyAttachment;
        this.handlerConstructor = handlerConstructor;
        this.defaultCreators = new Reference2ObjectArrayMap<>();
    }

    public int getContainerCount(ItemStack stack) {
        A attached = getOrEmpty(stack);
        if (attached.isEmpty()) {
            var containerCreator = defaultCreators.get(stack.getItem());
            return containerCreator == null ? 0 : containerCreator.get().totalContainers();
        }
        return attached.size();
    }

    public C createContainer(ItemStack attachedTo, int index) {
        var lazyCreator = defaultCreators.get(attachedTo.getItem());
        if (lazyCreator != null) {
            var creator = lazyCreator.get();
            return creator.create(index,
                    () -> attachedTo.getOrDefault(component, emptyAttachment),
                    attached -> attachedTo.set(component, attached),
                    () -> creator.initAttached(creator.totalContainers()));
        }
        throw new IllegalArgumentException("No known containers for item " + attachedTo.getItem());
    }

    public @Nullable H createHandler(ItemStack stack) {
        int count = getContainerCount(stack);
        if (count == 0) {
            return null;
        }
        return handlerConstructor.apply(stack, count);
    }

    public A getOrEmpty(ItemStack stack) {
        return stack.getOrDefault(component, emptyAttachment);
    }

    public void addDefaultCreators(@NotNull IEventBus bus, @NotNull Item item, @NotNull IContainerCreator<?, A, ? extends C> creator) {
        defaultCreators.put(item, Lazy.of(creator));
        bus.addListener(RegisterCapabilitiesEvent.class, event -> registerItemCapabilities(event, item, false));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerItemCapabilities(RegisterCapabilitiesEvent event, Item item, boolean exposeWhenStacked) {
        event.registerItem((ItemCapability) capability.item(), getCapabilityProvider(exposeWhenStacked), item);
    }

    protected ICapabilityProvider<ItemStack, Void, ? super H> getCapabilityProvider(boolean exposeWhenStacked) {
        if (exposeWhenStacked) {
            return (stack, context) -> createHandler(stack);
        }
        return (stack, context) -> stack.getCount() == 1 ? createHandler(stack) : null;
    }

    public boolean supports(ItemStack stack) {
        return stack.has(component) || defaultCreators.containsKey(stack.getItem());
    }

    public void addDefault(Holder<Item> item, DataComponentPatch.Builder builder) {
        var lazy = defaultCreators.get(item.value());
        if (lazy != null) {
            var containerCreator = lazy.get();
            int count = containerCreator.totalContainers();
            if (count > 0) {
                builder.set(component.get(), containerCreator.initAttached(count));
            }
        }
    }
}
