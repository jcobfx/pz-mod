package pl.pzmod.items.armor;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import pl.pzmod.PZMod;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TeslaHelmetItem extends ArmorItem {

    private static final Map<Type, Integer> DEFENSE_MAP = new EnumMap<>(ArmorItem.Type.class) {{
        put(ArmorItem.Type.HELMET, 2);
    }};

    private static final Holder<ArmorMaterial> TESLA_MATERIAL = new Holder.Direct<>(
            new ArmorMaterial(
                    DEFENSE_MAP,
                    9,
                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_EQUIP_IRON.value()),
                    () -> Ingredient.EMPTY,
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "tesla_helmet"),"",true
                    )),
                    0.0F,
                    0.0F
            )
    );

    public TeslaHelmetItem(Item.Properties properties) {
        super(TESLA_MATERIAL, Type.HELMET, properties.stacksTo(1));
    }
}