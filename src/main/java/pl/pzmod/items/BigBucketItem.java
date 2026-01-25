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

import java.util.function.BiPredicate;

public class BigBucketItem extends PZItem {
    public BigBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack bigBucket = player.getItemInHand(usedHand);
        IFluidHandler fluidTank = getFluidHandler(bigBucket);
        if (fluidTank == null) {
            return InteractionResultHolder.pass(bigBucket);
        }

        FluidStack fluidInTank = fluidTank.getFluidInTank(0);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player,
                fluidInTank.getAmount() < getCapacity() ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(bigBucket);
        }

        BlockPos blockpos = blockhitresult.getBlockPos();
        Direction direction = blockhitresult.getDirection();
        BlockPos relativeBlockpos = blockpos.relative(direction);
        if (!level.mayInteract(player, blockpos) || !player.mayUseItemAt(relativeBlockpos, direction, bigBucket)) {
            return InteractionResultHolder.fail(bigBucket);
        }

        FluidState fluidState = level.getFluidState(blockpos);
        BlockState blockstate = level.getBlockState(blockpos);
        if (fluidState.isSource() && canTankFitFluid(fluidTank, fluidState.getType())
                && blockstate.getBlock() instanceof BucketPickup bucketpickup
                && !bucketpickup.pickupBlock(player, level, blockpos, blockstate).isEmpty()) {
            bucketpickup.getPickupSound(blockstate).ifPresent(event -> player.playSound(event, 1.0F, 1.0F));
            fluidTank.fill(new FluidStack(fluidState.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
            return InteractionResultHolder.sidedSuccess(bigBucket, level.isClientSide());
        }

        BlockPos targetPos = canBlockContainFluid(player, level, blockpos, blockstate, fluidInTank.getFluid())
                ? blockpos : relativeBlockpos;
        if (!fluidInTank.isEmpty() && drainTank(player, level, targetPos, blockhitresult, fluidTank)) {
            fluidTank.drain(new FluidStack(fluidInTank.getFluid(), 1000), IFluidHandler.FluidAction.EXECUTE);
            playEmptySound(player, level, blockpos, fluidInTank);
            return InteractionResultHolder.sidedSuccess(bigBucket, level.isClientSide());
        }

        return InteractionResultHolder.fail(bigBucket);
    }

    private boolean drainTank(@Nullable Player player,
                              @NotNull Level level,
                              BlockPos pos,
                              @Nullable BlockHitResult result,
                              IFluidHandler fluidTank) {
        FluidStack fluidInTank = fluidTank.getFluidInTank(0);
        BlockState blockstate = level.getBlockState(pos);
        if (!canTankDrainFluid(fluidTank, fluidInTank.getFluid())) {
            return false;
        }

        if (!blockstate.isAir() && !blockstate.canBeReplaced(fluidInTank.getFluid())
                && !canBlockContainFluid(player, level, pos, blockstate, fluidInTank.getFluid())) {
            return result != null && drainTank(player, level,
                    result.getBlockPos().relative(result.getDirection()), null, fluidTank);
        }

        if (fluidInTank.getFluidType().isVaporizedOnPlacement(level, pos, fluidInTank)) {
            fluidInTank.getFluidType().onVaporize(player, level, pos, fluidInTank);
            return true;
        }

        if (level.dimensionType().ultraWarm() && fluidInTank.is(FluidTags.WATER)) {
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
                liquidBlockContainer.canPlaceLiquid(player, level, pos, blockstate, fluidInTank.getFluid())) {
            liquidBlockContainer.placeLiquid(level, pos, blockstate, ((FlowingFluid) fluidInTank.getFluid()).getSource(false));
            return true;
        }

        FluidState fluidstate = level.getFluidState(pos);
        if (!level.isClientSide() && blockstate.canBeReplaced() && fluidstate.isEmpty()) {
            level.destroyBlock(pos, true);
        }
        return level.setBlock(pos, fluidInTank.getFluid().defaultFluidState().createLegacyBlock(), 11)
                || blockstate.getFluidState().isSource();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getFluidAmount(stack) < getCapacity();
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        int maxCapacity = getCapacity();
        int fluidAmount = getFluidAmount(stack);
        return Math.round(fluidAmount * 13.0F / maxCapacity);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return Mth.hsvToRgb(1.0F / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public int getCapacity() {
        return 2000;
    }

    @Override
    public BiPredicate<Integer, @NotNull FluidStack> getValidator() {
        return (tank, stack) -> true;
    }

    private int getFluidAmount(@NotNull ItemStack stack) {
        IFluidHandler fluidTank = getFluidHandler(stack);
        if (fluidTank != null) {
            return fluidTank.getFluidInTank(0).getAmount();
        }
        return 0;
    }

    private @Nullable IFluidHandler getFluidHandler(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    private boolean canTankFitFluid(IFluidHandler tank, Fluid fluid) {
        FluidStack simulatedFill = new FluidStack(fluid, 1000);
        int filledAmount = tank.fill(simulatedFill, IFluidHandler.FluidAction.SIMULATE);
        return filledAmount >= 1000;
    }

    private boolean canTankDrainFluid(IFluidHandler tank, Fluid fluid) {
        FluidStack simulatedDrain = new FluidStack(fluid, 1000);
        FluidStack drainedStack = tank.drain(simulatedDrain, IFluidHandler.FluidAction.SIMULATE);
        return drainedStack.getAmount() >= 1000;
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
