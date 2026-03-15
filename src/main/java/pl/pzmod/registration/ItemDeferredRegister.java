package pl.pzmod.registration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemDeferredRegister extends PZDeferredRegister<Item> {
    public ItemDeferredRegister(String modid) {
        super(Registries.ITEM, modid, ItemRegistryObject::new);
    }

    public <I extends Item> ItemBuilder<I> builder(String name, Function<Item.Properties, I> factory) {
        return new ItemBuilder<>(name, factory);
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
        bus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Holder<? extends Item> entry : getEntries()) {
            if (entry instanceof ItemRegistryObject<?> blockEntityType) {
                blockEntityType.registerCapabilities(event);
            }
        }
    }

    public class ItemBuilder<I extends Item> {
        private final String name;
        private final Function<Item.Properties, I> factory;
        private final List<ItemRegistryObject.CapabilityData<?>> capabilities;

        private ItemBuilder(String name, Function<Item.Properties, I> factory) {
            this.name = name;
            this.factory = factory;
            this.capabilities = new ArrayList<>();
        }

        public <T> ItemBuilder<I> with(ItemCapability<T, Void> capability, ICapabilityProvider<ItemStack, Void, T> provider) {
            capabilities.add(new ItemRegistryObject.CapabilityData<>(capability, provider));
            return this;
        }

        public ItemRegistryObject<I> build() {
            var holder = register(name, () -> factory.apply(new Item.Properties()));
            holder.setCapabilities(capabilities);
            return holder;
        }
    }
}
