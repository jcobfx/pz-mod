package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;

public class PZItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PZMod.MODID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    private PZItems() {
    }
}
