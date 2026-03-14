package pl.pzmod.capabilities.fluids;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.data.containers.Action;
import pl.pzmod.data.containers.fluids.ISidedFluidHandler;
import pl.pzmod.data.containers.fluids.ISidedFluidHandlerItem;
import pl.pzmod.items.PZItem;

import java.util.Objects;

public class FluidCapabilityResolver implements ISidedFluidHandler {
    private final ISidedFluidHandler handler;
    private final @Nullable Direction side;

    public static ISidedFluidHandler forBlockEntity(@NotNull PZBlockEntity blockEntity, @Nullable Direction side) {
        return new FluidCapabilityResolver(Objects.requireNonNull(blockEntity.getFluidHandler(blockEntity)), side);
    }

    public static ISidedFluidHandlerItem forItem(@NotNull ItemStack stack, @NotNull PZItem item) {
        return new ItemFluidCapabilityResolver(Objects.requireNonNull(item.getFluidHandler(stack)), null, stack);
    }

    private FluidCapabilityResolver(@NotNull ISidedFluidHandler handler, @Nullable Direction side) {
        this.handler = handler;
        this.side = side;
    }

    @Override
    public @Nullable Direction getDefaultSide() {
        return side;
    }

    @Override
    public int getTanks(@Nullable Direction side) {
        return handler.getTanks(side);
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank, @Nullable Direction side) {
        return handler.getFluidInTank(tank, side);
    }

    @Override
    public int getTankCapacity(int tank, @Nullable Direction side) {
        return handler.getTankCapacity(tank, side);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack, @Nullable Direction side) {
        return handler.isFluidValid(tank, stack, side);
    }

    @Override
    public @NotNull FluidStack insertFluid(@NotNull FluidStack stack, @Nullable Direction side, @NotNull Action action) {
        return handler.insertFluid(stack, side, action);
    }

    @Override
    public @NotNull FluidStack extractFluid(@NotNull FluidStack stack, @Nullable Direction side, @NotNull Action action) {
        return handler.extractFluid(stack, side, action);
    }

    @Override
    public @NotNull FluidStack extractFluid(int amount, @Nullable Direction side, @NotNull Action action) {
        return handler.extractFluid(amount, side, action);
    }

    private static class ItemFluidCapabilityResolver extends FluidCapabilityResolver implements ISidedFluidHandlerItem {
        private final ItemStack stack;

        public ItemFluidCapabilityResolver(@NotNull ISidedFluidHandler handler, @Nullable Direction side, @NotNull ItemStack stack) {
            super(handler, side);
            this.stack = stack;
        }

        @Override
        public @NotNull ItemStack getContainer() {
            return stack;
        }
    }
}
