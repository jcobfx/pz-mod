package pl.pzmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.client.models.TeslaHelmetModel;
import pl.pzmod.client.renderer.ModModelLayers;
import pl.pzmod.registries.PZItems;
import pl.pzmod.registries.PZMenuTypes;
import pl.pzmod.menus.GeneratorScreen;

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

    @SubscribeEvent
    public static void registerClientScreens(RegisterMenuScreensEvent event){
        event.register(PZMenuTypes.GENERATOR.get(), GeneratorScreen::new);
    }

    @SubscribeEvent
    public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(@NotNull LivingEntity livingEntity,
                                                                   @NotNull ItemStack itemStack,
                                                                   @NotNull EquipmentSlot equipmentSlot,
                                                                   @NotNull HumanoidModel<?> original) {
                TeslaHelmetModel<?> model = new TeslaHelmetModel<>(
                        Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayers.TESLA_HELMET)
                );

                ((HumanoidModel) original).copyPropertiesTo(model);

                model.head.visible = (equipmentSlot == EquipmentSlot.HEAD);
                model.hat.visible = (equipmentSlot == EquipmentSlot.HEAD);

                return model;
            }
        }, PZItems.TESLA_HELMET.get());
    }

}
