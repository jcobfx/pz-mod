package pl.pzmod.capabilities.holder.fluid;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pzmod.capabilities.fluid.IFluidContainer;
import pl.pzmod.capabilities.holder.IHolder;

import java.util.List;

public interface IFluidHolder extends IHolder {
    @NotNull
    List<IFluidContainer> getFluidContainers(@Nullable Direction side);
}
