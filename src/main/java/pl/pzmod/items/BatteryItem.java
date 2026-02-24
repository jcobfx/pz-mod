package pl.pzmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.PZMod;

public class BatteryItem extends PZItem {
    public BatteryItem(Properties properties) {
        super(properties.stacksTo(1));
    }

        private static final TagKey<Block> BATTERY_CHARGERS =
                TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "battery_chargers"));

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
//                                                           @NotNull Player player,
//                                                           @NotNull InteractionHand usedHand) {
//        ItemStack itemInHand = player.getItemInHand(usedHand);
//        if (usedHand == InteractionHand.MAIN_HAND) {
//            IEnergyStorage energyStorage = getEnergyStorage(itemInHand);
//            if (energyStorage != null) {
//                if (player.isShiftKeyDown() && energyStorage.canExtract()) {
//                    energyStorage.extractEnergy(100, false);
//                    return InteractionResultHolder.success(itemInHand);
//                } else if (energyStorage.canReceive()) {
//                    energyStorage.receiveEnergy(100, false);
//                    return InteractionResultHolder.success(itemInHand);
//                }
//            }
//        }
//        return InteractionResultHolder.pass(itemInHand);
//    }



    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockState state = level.getBlockState(pos);
        if (!state.is(BATTERY_CHARGERS)) return InteractionResult.PASS;

        ItemStack batteryStack = context.getItemInHand();
        IEnergyStorage batteryCap = batteryStack.getCapability(Capabilities.EnergyStorage.ITEM);

        IEnergyStorage blockEnergy = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, context.getClickedFace());
        if (blockEnergy == null) {
            blockEnergy = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null);
        }

        if (batteryCap != null && blockEnergy != null) {
            int maxToReceive = Math.min(getEnergyMaxTransfer(), batteryCap.getMaxEnergyStored() - batteryCap.getEnergyStored());

            int available = blockEnergy.extractEnergy(maxToReceive, true);

            if (available > 0) {
                int accepted = batteryCap.receiveEnergy(available, false);
                blockEnergy.extractEnergy(accepted, false);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
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
        return 1000;
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
