package pl.pzmod.items;

import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public interface IContainerItem {
    void attachDefaultContainers(@NotNull IEventBus bus);
}
