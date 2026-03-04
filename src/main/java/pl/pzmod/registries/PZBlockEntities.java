package pl.pzmod.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import pl.pzmod.PZMod;
import pl.pzmod.blocks.entities.GeneratorBlockEntity;
import pl.pzmod.capabilities.energy.BlockEntityEnergyCapabilityResolver;
import pl.pzmod.capabilities.item.GeneratorBlockItemCapabilityResolver;

import java.util.function.Supplier;

@EventBusSubscriber(modid = PZMod.MODID)
public class PZBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PZMod.MODID);

    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR =
            BLOCK_ENTITIES.register("generator", () -> BlockEntityType.Builder.of(
                    GeneratorBlockEntity::new, PZBlocks.GENERATOR.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                GENERATOR.get(),
                BlockEntityEnergyCapabilityResolver::new
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                GENERATOR.get(),
                GeneratorBlockItemCapabilityResolver::new
        );
    }

    private PZBlockEntities() {
    }
}
