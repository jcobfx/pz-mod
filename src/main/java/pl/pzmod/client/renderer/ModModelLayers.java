package pl.pzmod.client.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import pl.pzmod.PZMod;
import pl.pzmod.client.models.TeslaHelmetModel;

@EventBusSubscriber(modid = PZMod.MODID, value = Dist.CLIENT)
public class ModModelLayers {

    public static final ModelLayerLocation TESLA_HELMET = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "tesla_helmet"), "main");

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TESLA_HELMET, TeslaHelmetModel::createBodyLayer);
    }
}