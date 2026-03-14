package pl.pzmod.data.containers;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IContainerHandler<T> {
    @NotNull
    List<T> getContainers(@Nullable Direction side);
}
