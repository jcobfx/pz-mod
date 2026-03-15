package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import pl.pzmod.PZMod;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.items.BackpackItem;
import pl.pzmod.items.BatteryItem;
import pl.pzmod.items.BigBucketItem;
import pl.pzmod.items.PZItem;
import pl.pzmod.registration.ItemDeferredRegister;
import pl.pzmod.registration.ItemRegistryObject;

public class PZItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(PZMod.MODID);

    public static final ItemRegistryObject<BatteryItem> BATTERY = ITEMS.builder("battery", BatteryItem::new)
            .with(Capabilities.ENERGY.item(), PZItem.ENERGY_HANDLER_PROVIDER)
            .build();

    public static final ItemRegistryObject<BackpackItem> BACKPACK = ITEMS.builder("backpack", BackpackItem::new)
            .with(Capabilities.ITEM.item(), PZItem.ITEM_HANDLER_PROVIDER)
            .build();

    public static final ItemRegistryObject<BigBucketItem> BIG_BUCKET = ITEMS.builder("big_bucket", BigBucketItem::new)
            .with(Capabilities.FLUID.item(), PZItem.FLUID_HANDLER_PROVIDER)
            .build();

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private PZItems() {
    }
}
