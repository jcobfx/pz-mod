package pl.pzmod;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import pl.pzmod.registries.*;

@Mod(PZMod.MODID)
public class PZMod {
    public static final String MODID = "pz_mod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PZMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        registerRegistries(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void registerRegistries(IEventBus eventBus) {
        PZCreativeTab.register(eventBus);
        PZDataComponents.register(eventBus);
        PZAttachments.register(eventBus);
        PZBlockEntities.register(eventBus);
        PZMenuTypes.register(eventBus);
        PZBlocks.register(eventBus);
        PZItems.register(eventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
