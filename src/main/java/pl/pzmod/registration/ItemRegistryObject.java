package pl.pzmod.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemRegistryObject<I extends Item> extends PZDeferredHolder<Item, I> {
    private @Nullable List<CapabilityData<?>> capabilities;

    protected ItemRegistryObject(ResourceKey<Item> key) {
        super(key);
    }

    void setCapabilities(@Nullable List<CapabilityData<?>> capabilities) {
        this.capabilities = capabilities;
    }

    void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (capabilities != null) {
            for (var cap : this.capabilities) {
                cap.register(event, value());
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    record CapabilityData<T>(ItemCapability<T, Void> capability,
                                             ICapabilityProvider<ItemStack, Void, T> provider) {
        private <I extends Item> void register(RegisterCapabilitiesEvent event, I item) {
            event.registerItem(capability, provider, item);
        }
    }
}
