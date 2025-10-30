package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;
import pl.pzmod.containers.energy.ItemEnergyHandler;
import pl.pzmod.items.Battery;

@EventBusSubscriber(modid = PZMod.MODID)
public class PZItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PZMod.MODID);

    public static final DeferredItem<Battery> BATTERY = ITEMS.registerItem("battery", Battery::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, access) -> new ItemEnergyHandler(stack, 10000, 100),
                BATTERY
        );
    }

    private PZItems() {
    }
}
