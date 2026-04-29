package pl.pzmod.registration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    public <I extends Item> ItemBuilder<I> builder(String name, Function<Item.Properties, I> sup) {
        return new ItemBuilder<>(name, sup);
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
        bus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Holder<Item> entry : getEntries()) {
            if (entry instanceof ItemRegistryObject<?> item) {
                item.registerCapabilities(event);
            }
        }
    }

    public class ItemBuilder<I extends Item> {
        private final String name;
        private final Function<Item.Properties, I> sup;
        private final List<ItemRegistryObject.CapabilityData<?>> capabilities;

        private ItemBuilder(String name, Function<Item.Properties, I> sup) {
            this.name = name;
            this.sup = sup;
            this.capabilities = new ArrayList<>();
        }

        public <T> ItemBuilder<I> with(ItemCapability<T, Void> capability, Function<ItemStack, T> provider) {
            capabilities.add(new ItemRegistryObject.CapabilityData<>(capability, provider));
            return this;
        }

        public ItemRegistryObject<I> build() {
            var holder = registerItem(name, sup);
            holder.setCapabilities(capabilities.isEmpty() ? null : capabilities);
            return holder;
        }
    }
}
