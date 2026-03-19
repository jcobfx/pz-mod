package pl.pzmod.items;

import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

public interface IContainerItem {
    void addDefaultContainers(@Nullable IEventBus bus);
}
