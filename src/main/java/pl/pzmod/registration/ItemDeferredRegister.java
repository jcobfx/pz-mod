package pl.pzmod.registration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ContainerType;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemDeferredRegister extends PZDeferredRegister<Item> {
    public ItemDeferredRegister(String modid) {
        super(Registries.ITEM, modid, ItemRegistryObject::new);
    }

    public <I extends Item> ItemRegistryObject<I> registerItem(String name, Function<Item.Properties, I> sup) {
        return register(name, () -> sup.apply(new Item.Properties()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <I extends Item> ItemRegistryObject<I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (ItemRegistryObject<I>) super.register(name, sup);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <I extends Item> ItemRegistryObject<I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (ItemRegistryObject<I>) super.register(name, func);
    }

    @Override
    public void register(@NotNull IEventBus bus) {
        super.register(bus);
        bus.addListener(EventPriority.LOWEST, RegisterEvent.class, event -> {
            if (event.getRegistryKey().equals(Registries.ITEM)) {
                forEntries(registryObject -> registryObject.attachDefaultContainers(bus));
            }
        });
        bus.addListener(EventPriority.LOWEST, ModifyDefaultComponentsEvent.class, event ->
                forEntries(registryObject -> {
                    if (ContainerType.anySupports(registryObject)) {
                        event.modify(registryObject, builder -> {
                            for (ContainerType<?, ?, ?> type : ContainerType.TYPES) {
                                type.addDefault(registryObject, builder);
                            }
                        });
                    }
                }));
    }

    private void forEntries(Consumer<ItemRegistryObject<?>> consumer) {
        for (Holder<Item> entry : getEntries()) {
            if (entry instanceof ItemRegistryObject<?> registryObject) {
                consumer.accept(registryObject);
            }
        }
    }
}
