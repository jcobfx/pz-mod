package pl.pzmod.items;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BatteryItem extends PZItem {
    public BatteryItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand usedHand) {
        ItemStack itemInHand = player.getItemInHand(usedHand);
        if (usedHand == InteractionHand.MAIN_HAND) {
            IEnergyStorage energyStorage = getEnergyStorage(itemInHand);
            if (energyStorage != null) {
                if (player.isShiftKeyDown() && energyStorage.canExtract()) {
                    energyStorage.extractEnergy(100, false);
                    return InteractionResultHolder.success(itemInHand);
                } else if (energyStorage.canReceive()) {
                    energyStorage.receiveEnergy(100, false);
                    return InteractionResultHolder.success(itemInHand);
                }
            }
        }
        return InteractionResultHolder.pass(itemInHand);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getEnergy(stack) < getEnergyCapacity();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int maxEnergy = getEnergyCapacity();
        int energy = getEnergy(stack);
        return Math.round(energy * 13.0F / maxEnergy);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return Mth.hsvToRgb(1.0F / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getEnergyCapacity() {
        return 10000;
    }

    @Override
    public int getEnergyMaxTransfer() {
        return 100;
    }

    private int getEnergy(@NotNull ItemStack stack) {
        IEnergyStorage energyStorage = getEnergyStorage(stack);
        if (energyStorage != null) {
            return energyStorage.getEnergyStored();
        }
        return 0;
    }

    private @Nullable IEnergyStorage getEnergyStorage(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM);
    }
}
