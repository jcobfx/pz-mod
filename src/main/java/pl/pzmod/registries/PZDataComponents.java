package pl.pzmod.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.data.containers.AttachedFluids;
import pl.pzmod.data.containers.AttachedItems;

public class PZDataComponents {
    private static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PZMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AttachedItems>> ITEMS_COMPONENT =
            DATA_COMPONENTS.registerComponentType("items_component", builder -> builder
                    .persistent(AttachedItems.CODEC)
                    .networkSynchronized(AttachedItems.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AttachedEnergy>> ENERGY_COMPONENT =
            DATA_COMPONENTS.registerComponentType("energy_component", builder -> builder
                    .persistent(AttachedEnergy.CODEC)
                    .networkSynchronized(AttachedEnergy.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AttachedFluids>> FLUIDS_COMPONENT =
            DATA_COMPONENTS.registerComponentType("fluid_component", builder -> builder
                    .persistent(AttachedFluids.CODEC)
                    .networkSynchronized(AttachedFluids.STREAM_CODEC));

    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
    }

    private PZDataComponents() {
    }
}
