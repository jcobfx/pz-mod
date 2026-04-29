package pl.pzmod.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ItemRegistryObject<I extends Item> extends PZDeferredHolder<Item, I> implements ItemLike {
    private @Nullable List<CapabilityData<?>> capabilities;

    protected ItemRegistryObject(ResourceKey<Item> key) {
        super(key);
    }

    @Override
    public @NotNull Item asItem() {
        return value();
    }

    void setCapabilities(@Nullable List<CapabilityData<?>> capabilities) {
        this.capabilities = capabilities;
    }

    void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (capabilities != null) {
            for (var cap : capabilities) {
                cap.register(event, value());
            }
        }
    }

    record CapabilityData<T>(ItemCapability<T, Void> capability, Function<ItemStack, T> provider) {
        private void register(RegisterCapabilitiesEvent event, ItemLike item) {
            event.registerItem(capability, (stack, ctx) -> provider.apply(stack), item);
        }
    }
}
