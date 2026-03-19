package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import pl.pzmod.PZMod;
import pl.pzmod.blocks.entities.CapabilityBlockEntity;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.registration.BlockEntityTypeDeferredRegister;
import pl.pzmod.registration.BlockEntityTypeRegistryObject;

public class PZBlockEntities {
    private static final BlockEntityTypeDeferredRegister BLOCK_ENTITIES = new BlockEntityTypeDeferredRegister(PZMod.MODID);

    public static final BlockEntityTypeRegistryObject<GeneratorBlockEntity> GENERATOR =
            BLOCK_ENTITIES.builder(PZBlocks.GENERATOR, GeneratorBlockEntity::new)
                    .with(Capabilities.ENERGY.block(), CapabilityBlockEntity.ENERGY_HANDLER_PROVIDER)
                    .with(Capabilities.ITEM.block(), CapabilityBlockEntity.ITEM_HANDLER_PROVIDER)
                    .serverTicker(GeneratorBlockEntity::tickServer)
                    .build();

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    private PZBlockEntities() {
    }
}
