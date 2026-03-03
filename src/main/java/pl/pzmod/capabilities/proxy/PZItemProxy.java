package pl.pzmod.capabilities.proxy;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.items.PZItem;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class PZItemProxy extends Proxy<ItemStack> {
    private final PZItem item;

    public PZItemProxy(ItemStack itemStack) {
        super(itemStack);
        this.item = (PZItem) itemStack.getItem();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D, T> D getData(Supplier<? extends T> dataType, D defaultValue) {
        return getParent().getOrDefault((Supplier<? extends DataComponentType<? extends D>>) dataType, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D, T> void setData(Supplier<? extends T> dataType, D value) {
        getParent().set((Supplier<? extends DataComponentType<? super D>>) dataType, value);
    }

    @Override
    public int getEnergyCapacity() {
        return item.getEnergyCapacity();
    }

    @Override
    public int getEnergyMaxTransfer() {
        return item.getEnergyMaxTransfer();
    }

    @Override
    public int getTankCount() {
        return item.getTankCount();
    }

    @Override
    public int getTankCapacity() {
        return item.getTankCapacity();
    }

    @Override
    public BiPredicate<Integer, @NotNull FluidStack> getFluidValidator() {
        return item.getFluidValidator();
    }

    @Override
    public int getSlotCount() {
        return item.getSlotCount();
    }

    @Override
    public int getSlotLimit() {
        return item.getSlotLimit();
    }
}
