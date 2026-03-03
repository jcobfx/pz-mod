package pl.pzmod.capabilities.proxy;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.blocks.PZBlock;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class PZBlockProxy extends Proxy<BlockEntity> {
    private final PZBlock block;

    public PZBlockProxy(BlockEntity parent) {
        super(parent);
        this.block = (PZBlock) parent.getBlockState().getBlock();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D, T> D getData(Supplier<? extends T> dataType, D defaultValue) {
        return getParent().getData((Supplier<AttachmentType<D>>) dataType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D, T> void setData(Supplier<? extends T> dataType, D value) {
        getParent().setData((Supplier<AttachmentType<D>>) dataType, value);
    }


    @Override
    public int getEnergyCapacity() {
        return block.getEnergyCapacity();
    }

    @Override
    public int getEnergyMaxTransfer() {
        return block.getEnergyMaxTransfer();
    }

    @Override
    public int getTankCount() {
        return block.getTankCount();
    }

    @Override
    public int getTankCapacity() {
        return block.getTankCapacity();
    }

    @Override
    public BiPredicate<Integer, @NotNull FluidStack> getFluidValidator() {
        return block.getFluidValidator();
    }

    @Override
    public int getSlotCount() {
        return block.getSlotCount();
    }

    @Override
    public int getSlotLimit() {
        return block.getSlotLimit();
    }
}
