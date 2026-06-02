package pl.pzmod.registries;

import net.minecraft.core.component.DataComponents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;
import pl.pzmod.PZMod;
import pl.pzmod.items.BackpackItem;
import pl.pzmod.items.BatteryItem;
import pl.pzmod.items.BigBucketItem;
import pl.pzmod.items.armor.TeslaHelmetItem;
import pl.pzmod.registration.ItemDeferredRegister;
import pl.pzmod.registration.ItemRegistryObject;

public class PZItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(PZMod.MODID);

    public static final ItemRegistryObject<BatteryItem> BATTERY = ITEMS.builder("battery", BatteryItem::new)
            .with(Capabilities.EnergyStorage.ITEM, stack -> new ComponentEnergyStorage(stack, PZDataComponents.ENERGY_CONTAINER.get(), BatteryItem.CAPACITY, BatteryItem.RATE))
            .build();

    public static final ItemRegistryObject<BackpackItem> BACKPACK = ITEMS.builder("backpack", BackpackItem::new)
            .with(Capabilities.ItemHandler.ITEM, stack -> new ComponentItemHandler(stack, DataComponents.CONTAINER, BackpackItem.SLOTS))
            .build();

    public static final ItemRegistryObject<BigBucketItem> BIG_BUCKET = ITEMS.builder("big_bucket", BigBucketItem::new)
            .with(Capabilities.FluidHandler.ITEM, stack -> new FluidHandlerItemStack(PZDataComponents.FLUID_CONTAINER, stack, BigBucketItem.CAPACITY))
            .build();

    public static final ItemRegistryObject<TeslaHelmetItem> TESLA_HELMET = ITEMS.builder("tesla_helmet", TeslaHelmetItem::new)
            .build();

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private PZItems() {
    }
}
