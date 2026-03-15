package pl.pzmod.registration;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class PZDeferredRegister<T> extends DeferredRegister<T> {
    private final Function<ResourceKey<T>, ? extends PZDeferredHolder<T, ?>> holderCreator;

    public PZDeferredRegister(ResourceKey<? extends Registry<T>> registry, String namespace) {
        this(registry, namespace, PZDeferredHolder::new);
    }

    public PZDeferredRegister(ResourceKey<? extends Registry<T>> registryKey,
                              String namespace,
                              Function<ResourceKey<T>, ? extends PZDeferredHolder<T, ? extends T>> holderCreator) {
        super(registryKey, namespace);
        this.holderCreator = holderCreator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> @NotNull PZDeferredHolder<T, I> register(@NotNull String name, @NotNull Function<ResourceLocation, ? extends I> func) {
        return (PZDeferredHolder<T, I>) super.register(name, func);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> @NotNull PZDeferredHolder<T, I> register(@NotNull String name, @NotNull Supplier<? extends I> sup) {
        return (PZDeferredHolder<T, I>) super.register(name, sup);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <I extends T> @NotNull PZDeferredHolder<T, I> createHolder(@NotNull ResourceKey<? extends Registry<T>> registryKey,
                                                                         @NotNull ResourceLocation key) {
        return (PZDeferredHolder<T, I>) holderCreator.apply(ResourceKey.create(registryKey, key));
    }
}
