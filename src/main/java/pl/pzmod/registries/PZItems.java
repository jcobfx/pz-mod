package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;
import pl.pzmod.capabilities.energy.ItemEnergyCapabilityResolver;
import pl.pzmod.capabilities.item.ItemItemCapabilityResolver;
import pl.pzmod.items.BackpackItem;
import pl.pzmod.items.BatteryItem;

@EventBusSubscriber(modid = PZMod.MODID)
public class PZItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PZMod.MODID);

    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.registerItem("battery", BatteryItem::new);

    public static final DeferredItem<BackpackItem> BACKPACK = ITEMS.registerItem("backpack", BackpackItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                ItemEnergyCapabilityResolver::new,
                BATTERY
        );

        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                ItemItemCapabilityResolver::new,
                BACKPACK
        );
    }

    private PZItems() {
    }
}
