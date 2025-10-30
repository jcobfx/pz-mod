package pl.pzmod.items;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BatteryItem extends PZItem {
    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        IEnergyStorage energyStorage = getEnergyStorage(context.getItemInHand());
        Player player = context.getPlayer();
        if (energyStorage != null && player != null) {
            if (player.isShiftKeyDown() && energyStorage.canExtract()) {
                energyStorage.extractEnergy(100, false);
            } else if (energyStorage.canReceive()) {
                energyStorage.receiveEnergy(100, false);
            }
        }
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getEnergy(stack) < getEnergyCapacity();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int maxEnergy = getEnergyCapacity();
        int energy = getEnergy(stack);
        return Math.round((float) energy * 13.0F / (float) maxEnergy);
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

    private @Nullable IEnergyStorage getEnergyStorage(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM);
    }

    private int getEnergy(@NotNull ItemStack stack) {
        IEnergyStorage energyStorage = getEnergyStorage(stack);
        if (energyStorage != null) {
            return energyStorage.getEnergyStored();
        }
        return 0;
    }
}
