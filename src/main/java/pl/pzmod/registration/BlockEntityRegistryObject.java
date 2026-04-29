package pl.pzmod.registration;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEntityRegistryObject<E extends BlockEntity> extends PZDeferredHolder<BlockEntityType<?>, BlockEntityType<E>> {
    private @Nullable List<CapabilityData<E, ?, ?>> capabilities;
    private @Nullable BlockEntityTicker<E> clientTicker;
    private @Nullable BlockEntityTicker<E> serverTicker;

    protected BlockEntityRegistryObject(ResourceKey<BlockEntityType<?>> key) {
        super(key);
    }

    public @Nullable BlockEntityTicker<E> getTicker(boolean isClient) {
        return isClient ? clientTicker : serverTicker;
    }

    void setCapabilities(@Nullable List<CapabilityData<E, ?, ?>> capabilities) {
        this.capabilities = capabilities;
    }

    void setTickers(@Nullable BlockEntityTicker<E> clientTicker, @Nullable BlockEntityTicker<E> serverTicker) {
        this.clientTicker = clientTicker;
        this.serverTicker = serverTicker;
    }

    void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (capabilities != null) {
            for (var cap : capabilities) {
                cap.register(event, value());
            }
        }
    }

    record CapabilityData<E extends BlockEntity, T, C>(BlockCapability<T, C> capability,
                                                       ICapabilityProvider<? super E, C, T> provider) {
        private void register(RegisterCapabilitiesEvent event, BlockEntityType<E> type) {
            event.registerBlockEntity(capability, type, provider);
        }
    }
}
