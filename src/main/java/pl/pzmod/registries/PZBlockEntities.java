package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import pl.pzmod.PZMod;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;
import pl.pzmod.registration.BlockEntityDeferredRegister;
import pl.pzmod.registration.BlockEntityRegistryObject;

public class PZBlockEntities {
    private static final BlockEntityDeferredRegister BLOCK_ENTITIES = new BlockEntityDeferredRegister(PZMod.MODID);

    public static final BlockEntityRegistryObject<GeneratorBlockEntity> GENERATOR =
            BLOCK_ENTITIES.builder(PZBlocks.GENERATOR, GeneratorBlockEntity::new)
                    .with(Capabilities.EnergyStorage.BLOCK, (blockEntity, side) -> blockEntity)
                    .with(Capabilities.ItemHandler.BLOCK, (blockEntity, side) -> new InvWrapper(blockEntity))
                    .serverTicker(GeneratorBlockEntity::tickServer)
                    .build();

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    private PZBlockEntities() {
    }
}
