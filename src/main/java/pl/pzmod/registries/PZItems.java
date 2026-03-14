package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;
import pl.pzmod.capabilities.energy.EnergyCapabilityResolver;
import pl.pzmod.capabilities.fluids.FluidCapabilityResolver;
import pl.pzmod.capabilities.items.ItemCapabilityResolver;
import pl.pzmod.items.BackpackItem;
import pl.pzmod.items.BatteryItem;
import pl.pzmod.items.BigBucketItem;
import pl.pzmod.items.PZItem;

@EventBusSubscriber(modid = PZMod.MODID)
public class PZItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PZMod.MODID);

    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.registerItem("battery", BatteryItem::new);

    public static final DeferredItem<BackpackItem> BACKPACK = ITEMS.registerItem("backpack", BackpackItem::new);

    public static final DeferredItem<BigBucketItem> BIG_BUCKET = ITEMS.registerItem("big_bucket", BigBucketItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, ctx) -> EnergyCapabilityResolver.forItem(stack, (PZItem) stack.getItem()),
                BATTERY
        );

        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (stack, ctx) -> ItemCapabilityResolver.forItem(stack, (PZItem) stack.getItem()),
                BACKPACK
        );

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> FluidCapabilityResolver.forItem(stack, (PZItem) stack.getItem()),
                BIG_BUCKET
        );
    }

    private PZItems() {
    }
}
