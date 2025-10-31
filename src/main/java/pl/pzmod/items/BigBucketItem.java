package pl.pzmod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
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

        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player,
                fluidTank.getFluidInTank(0).isEmpty() ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(bigBucket);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(bigBucket);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos relativeBlockpos = blockpos.relative(direction);
            FluidStack fluidInTank = fluidTank.getFluidInTank(0);
            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(relativeBlockpos, direction, bigBucket)) {
                BlockState blockstate = level.getBlockState(blockpos);
                if (fluidInTank.isEmpty()) {
                    return InteractionResultHolder.fail(bigBucket);
                } else {
                    BlockPos targetBlockpos = canBlockContainFluid(player, level, blockpos, blockstate, fluidInTank.getFluid())
                            ? blockpos : relativeBlockpos;
                    if (this.emptyContents(player, level, targetBlockpos, blockhitresult, fluidInTank)) {
                        this.checkExtraContent(player, level, bigBucket, targetBlockpos);
                        return InteractionResultHolder.success(bigBucket);
                    } else {
                        return InteractionResultHolder.fail(bigBucket);
                    }
                }
            } else {
                return InteractionResultHolder.fail(bigBucket);
            }
        }
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

    private @Nullable IFluidHandler getFluidHandler(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    protected boolean canBlockContainFluid(@Nullable Player player,
                                           Level worldIn,
                                           BlockPos posIn,
                                           BlockState blockstate,
                                           Fluid content) {
        return blockstate.getBlock() instanceof LiquidBlockContainer
                && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(player, worldIn, posIn, blockstate, content);
    }

    public boolean emptyContents(@Nullable Player player,
                                 Level level,
                                 BlockPos pos,
                                 @Nullable BlockHitResult result,
                                 @NotNull FluidStack contents) {
        if (!(contents.getFluid() instanceof FlowingFluid flowingfluid)) {
            return false;
        }
    }
}
