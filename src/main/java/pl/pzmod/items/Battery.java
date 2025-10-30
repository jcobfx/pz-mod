package pl.pzmod.items;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.AttachedEnergy;

import static pl.pzmod.registries.PZDataComponents.ATTACHED_ENERGY;

public class Battery extends PZItem {
    public Battery(Properties properties) {
        super(properties.stacksTo(1));
    }

    public int getMaxEnergy() {
        return 10000;
    }

    public int getEnergy(@NotNull ItemStack stack) {

        return stack.getOrDefault(ATTACHED_ENERGY, AttachedEnergy.empty()).energy();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getEnergy(stack) < getMaxEnergy();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int maxEnergy = getMaxEnergy();
        int energy = getEnergy(stack);
        return Math.round((float) energy * 13.0F / (float) maxEnergy);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        int maxEnergy = getMaxEnergy();
        int energy = getEnergy(stack);
        float f = Math.max(0.0F, (maxEnergy - (float) energy) / maxEnergy);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
