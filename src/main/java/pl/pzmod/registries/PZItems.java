package pl.pzmod.registries;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.ComponentItemHandler;
import pl.pzmod.PZMod;
import pl.pzmod.items.BackpackItem;
import pl.pzmod.items.BatteryItem;
import pl.pzmod.items.armor.TeslaHelmetItem;
import pl.pzmod.items.BuckChucketsItem;
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

    public static final ItemRegistryObject<BuckChucketsItem> BUCK_CHUCKETS = ITEMS.builder("buck_chuckets", BuckChucketsItem::new)
            .with(Capabilities.FluidHandler.ITEM, stack -> new FluidHandlerItemStack(PZDataComponents.FLUID_CONTAINER, stack, BuckChucketsItem.CAPACITY))
            .build();

    public static final ItemRegistryObject<TeslaHelmetItem> TESLA_HELMET = ITEMS.builder("tesla_helmet", TeslaHelmetItem::new)
            .build();

    public static final ItemRegistryObject<Item> ENERGY_CELL = ITEMS.registerItem("energy_cell", props ->
            new Item(props.stacksTo(16)));

    public static final ItemRegistryObject<Item> COIL = ITEMS.registerItem("coil", Item::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private PZItems() {
    }
}
