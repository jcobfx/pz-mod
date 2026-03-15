package pl.pzmod.registration;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PZDeferredHolder<R, T extends R> extends DeferredHolder<R, T> {
    public PZDeferredHolder(ResourceKey<R> key) {
        super(key);
    }
}
