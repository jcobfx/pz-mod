package pl.pzmod.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.items.IContainerItem;

public class ItemRegistryObject<I extends Item> extends PZDeferredHolder<Item, I> implements ItemLike {
    protected ItemRegistryObject(ResourceKey<Item> key) {
        super(key);
    }

    void attachDefaultContainers(@NotNull IEventBus eventBus) {
        if (get() instanceof IContainerItem containerItem) {
            containerItem.addDefaultContainers(eventBus);
        }
    }

    @Override
    public @NotNull Item asItem() {
        return value();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
