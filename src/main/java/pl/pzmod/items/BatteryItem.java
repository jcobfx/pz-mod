package pl.pzmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.PZMod;

public class BatteryItem extends Item {
    public static final int CAPACITY = 10000;
    public static final int RATE = 1000;

    private static final TagKey<Block> BATTERY_CHARGERS =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "battery_chargers"));

    public BatteryItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(BATTERY_CHARGERS)) {
            return InteractionResult.PASS;
        }

        IEnergyStorage blockCap = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, null, context.getClickedFace());
        if (blockCap == null) {
            blockCap = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, null, null);
        }
        IEnergyStorage batteryCap = context.getItemInHand().getCapability(Capabilities.EnergyStorage.ITEM);
        if (batteryCap == null || blockCap == null) {
            return InteractionResult.FAIL;
        }

        int toTransfer = batteryCap.receiveEnergy(RATE, true);
        toTransfer = blockCap.extractEnergy(toTransfer, true);
        if (toTransfer > 0) {
            batteryCap.receiveEnergy(toTransfer, false);
            blockCap.extractEnergy(toTransfer, false);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
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
