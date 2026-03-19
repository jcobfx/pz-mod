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

    public static final ItemRegistryObject<BatteryItem> BATTERY = ITEMS.registerItem("battery", BatteryItem::new);

    public static final ItemRegistryObject<BackpackItem> BACKPACK = ITEMS.registerItem("backpack", BackpackItem::new);

    public static final ItemRegistryObject<BigBucketItem> BIG_BUCKET = ITEMS.registerItem("big_bucket", BigBucketItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private PZItems() {
    }
}
