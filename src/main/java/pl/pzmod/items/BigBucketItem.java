package pl.pzmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BigBucketItem extends Item {
    public static final int CAPACITY = 2000;

    public BigBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack bigBucket = player.getItemInHand(usedHand);
        IFluidHandler fluidCap = bigBucket.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidCap == null) {
            return InteractionResultHolder.pass(bigBucket);
        }

        FluidStack stored = fluidCap.getFluidInTank(0);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player,
                stored.getAmount() < CAPACITY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(bigBucket);
        }

        BlockPos blockpos = blockHitResult.getBlockPos();
        Direction direction = blockHitResult.getDirection();
        BlockPos relativeBlockpos = blockpos.relative(direction);
        if (!level.mayInteract(player, blockpos) || !player.mayUseItemAt(relativeBlockpos, direction, bigBucket)) {
            return InteractionResultHolder.fail(bigBucket);
        }

        FluidState fluidState = level.getFluidState(blockpos);
        BlockState blockstate = level.getBlockState(blockpos);
        FluidStack toFill = new FluidStack(fluidState.getType(), 1000);
        if (fluidState.isSource() && canInsertFluid(fluidCap, toFill) &&
                blockstate.getBlock() instanceof BucketPickup bucketpickup &&
                !bucketpickup.pickupBlock(player, level, blockpos, blockstate).isEmpty()) {
            bucketpickup.getPickupSound(blockstate).ifPresent(event -> player.playSound(event, 1.0F, 1.0F));
            fluidCap.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
            return InteractionResultHolder.sidedSuccess(bigBucket, level.isClientSide());
        }

        BlockPos targetPos = canBlockContainFluid(player, level, blockpos, blockstate, stored.getFluid()) ?
                blockpos : relativeBlockpos;
        if (!stored.isEmpty() && extractFromTank(player, level, targetPos, blockHitResult, fluidCap)) {
            fluidCap.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            playEmptySound(player, level, blockpos, stored);
            return InteractionResultHolder.sidedSuccess(bigBucket, level.isClientSide());
        }

        return InteractionResultHolder.fail(bigBucket);
    }

    private boolean extractFromTank(@Nullable Player player,
                                    @NotNull Level level,
                                    BlockPos pos,
                                    @Nullable BlockHitResult result,
                                    IFluidHandler fluidCap) {
        FluidStack stored = fluidCap.getFluidInTank(0);
        BlockState blockstate = level.getBlockState(pos);
        if (!canExtractFluid(fluidCap)) {
            return false;
        }

        Fluid fluid = stored.getFluid();
        if (!blockstate.isAir() && !blockstate.canBeReplaced(fluid)
                && !canBlockContainFluid(player, level, pos, blockstate, fluid)) {
            return result != null && extractFromTank(player, level, result.getBlockPos().relative(result.getDirection()), null, fluidCap);
        }

        if (stored.getFluidType().isVaporizedOnPlacement(level, pos, stored)) {
            stored.getFluidType().onVaporize(player, level, pos, stored);
            return true;
        }

        if (level.dimensionType().ultraWarm() && stored.is(FluidTags.WATER)) {
            int l = pos.getX();
            int i = pos.getY();
            int j = pos.getZ();
            level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
                    2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
            for (int k = 0; k < 8; ++k) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, l + Math.random(), i + Math.random(), j + Math.random(),
                        0.0, 0.0, 0.0);
            }
            return true;
        }

        if (blockstate.getBlock() instanceof LiquidBlockContainer liquidBlockContainer &&
                liquidBlockContainer.canPlaceLiquid(player, level, pos, blockstate, fluid)) {
            liquidBlockContainer.placeLiquid(level, pos, blockstate, ((FlowingFluid) fluid).getSource(false));
            return true;
        }

        FluidState fluidstate = level.getFluidState(pos);
        if (!level.isClientSide() && blockstate.canBeReplaced() && fluidstate.isEmpty()) {
            level.destroyBlock(pos, true);
        }
        return level.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), 11)
                || blockstate.getFluidState().isSource();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        IFluidHandler fluidCap = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidCap == null) {
            return false;
        }
        return fluidCap.getFluidInTank(0).getAmount() < CAPACITY;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        IFluidHandler fluidCap = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (fluidCap == null) {
            return 0;
        }
        return Math.round(fluidCap.getFluidInTank(0).getAmount() * 13.0F / CAPACITY);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return Mth.hsvToRgb(1.0F / 3.0F, 1.0F, 1.0F);
    }

    private boolean canInsertFluid(IFluidHandler fluidCap, FluidStack stack) {
        return fluidCap.fill(stack, IFluidHandler.FluidAction.SIMULATE) > 0;
    }

    private boolean canExtractFluid(IFluidHandler fluidCap) {
        return fluidCap.drain(1000, IFluidHandler.FluidAction.SIMULATE).getAmount() >= 1000;
    }

    private boolean canBlockContainFluid(@Nullable Player player,
                                         Level level,
                                         BlockPos pos,
                                         BlockState blockstate,
                                         Fluid fluid) {
        return blockstate.getBlock() instanceof LiquidBlockContainer liquidBlockContainer
                && liquidBlockContainer.canPlaceLiquid(player, level, pos, blockstate, fluid);
    }

    private void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos, FluidStack fluidStack) {
        SoundEvent soundevent = fluidStack.getFluidType().getSound(player, level, pos, SoundActions.BUCKET_EMPTY);
        if (soundevent == null) {
            soundevent = fluidStack.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }

        level.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
