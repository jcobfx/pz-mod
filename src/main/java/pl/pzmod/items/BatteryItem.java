package pl.pzmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.PZMod;
import pl.pzmod.capabilities.Capabilities;
import pl.pzmod.capabilities.ContainerHandlerHelper;
import pl.pzmod.capabilities.energy.EnergyContainerConfig;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.energy.EnergyHandler;

public class BatteryItem extends PZItem {
    private static final int CAPACITY = 10000;
    private static final int RATE = 1000;

    private static final TagKey<Block> BATTERY_CHARGERS =
            TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(PZMod.MODID, "battery_chargers"));

    public BatteryItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    protected @Nullable EnergyHandler getInitialEnergyHandler(@NotNull ItemStack stack) {
        return ContainerHandlerHelper.builder(new EnergyHandler(), IContainerHolder.from(stack, 1))
                .addContainer(EnergyContainerConfig.inout(() -> CAPACITY, () -> RATE))
                .build();
    }

    @Override
    public boolean canHandleEnergy() {
        return true;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockState state = level.getBlockState(pos);
        if (!state.is(BATTERY_CHARGERS)) return InteractionResult.PASS;

        IEnergyStorage batteryCap = Capabilities.ENERGY.getCapability(context.getItemInHand());
        IEnergyStorage blockCap = Capabilities.ENERGY.getCapability(level, pos, context.getClickedFace());
        if (blockCap == null) {
            blockCap = Capabilities.ENERGY.getCapability(level, pos, null);
        }

        if (batteryCap != null && blockCap != null) {
            int maxToReceive = Math.min(RATE, batteryCap.getMaxEnergyStored() - batteryCap.getEnergyStored());

            int available = blockCap.extractEnergy(maxToReceive, true);

            if (available > 0) {
                int accepted = batteryCap.receiveEnergy(available, false);
                blockCap.extractEnergy(accepted, false);

                return InteractionResult.SUCCESS;
            }
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
        IEnergyStorage energyStorage = Capabilities.ENERGY.getCapability(stack);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }
}
