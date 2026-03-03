package pl.pzmod;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import pl.pzmod.registries.PZMenuTypes;
import pl.pzmod.screen.generator.GeneratorScreen;

@Mod(value = PZMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = PZMod.MODID, value = Dist.CLIENT)
public class PZModClient {
    public PZModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        PZMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        PZMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }

    @EventBusSubscriber(modid = PZMod.MODID, value = Dist.CLIENT)
    public static class ClientModEvents{
        @SubscribeEvent
        public static void registerClientScreens(RegisterMenuScreensEvent event){
            event.register(PZMenuTypes.GENERATOR.get(), GeneratorScreen::new);
        }

        private ClientModEvents() {
        }
    }
}
