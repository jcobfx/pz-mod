package pl.pzmod.items.curios;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class NightVisionGogglesItem extends Item implements ICurioItem {
    public static final int CAPACITY = 100000;

    public NightVisionGogglesItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.cosmetic()) {
            return;
        }

        var currentEffect = slotContext.entity().getEffect(MobEffects.NIGHT_VISION);
        IEnergyStorage energyCap = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyCap == null || energyCap.extractEnergy(1, true) <= 0 ||
                currentEffect == null || currentEffect.getDuration() > 400) {
            return;
        }
        energyCap.extractEnergy(1, false);

        MobEffectInstance nightVisionEffect = new MobEffectInstance(
                MobEffects.NIGHT_VISION,
                600,
                0,
                false,
                false,
                false
        );
        slotContext.entity().addEffect(nightVisionEffect);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getEnergy(stack) < CAPACITY;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int energy = getEnergy(stack);
        return Math.round(energy * 13.0F / CAPACITY);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return Mth.hsvToRgb(1.0F / 3.0F, 1.0F, 1.0F);
    }

    private int getEnergy(ItemStack stack) {
        IEnergyStorage energyCap = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyCap != null ? energyCap.getEnergyStored() : 0;
    }
}
