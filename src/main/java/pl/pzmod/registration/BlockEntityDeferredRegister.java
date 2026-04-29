package pl.pzmod.registration;

import com.google.common.base.Preconditions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityDeferredRegister extends PZDeferredRegister<BlockEntityType<?>> {
    public BlockEntityDeferredRegister(String modid) {
        super(Registries.BLOCK_ENTITY_TYPE, modid, BlockEntityRegistryObject::new);
    }

    public <E extends BlockEntity> BlockEntityTypeBuilder<E> builder(DeferredHolder<Block, ?> block,
                                                                     BlockEntityType.BlockEntitySupplier<? extends E> factory) {
        return new BlockEntityTypeBuilder<>(block, factory);
    }

    @Override
    public void register(@NotNull IEventBus bus) {
        super.register(bus);
        bus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Holder<BlockEntityType<?>> entry : getEntries()) {
            if (entry instanceof BlockEntityRegistryObject<?> blockEntityType) {
                blockEntityType.registerCapabilities(event);
            }
        }
    }

    public class BlockEntityTypeBuilder<E extends BlockEntity> {
        private final DeferredHolder<Block, ?> block;
        private final BlockEntityType.BlockEntitySupplier<? extends E> factory;
        private final List<BlockEntityRegistryObject.CapabilityData<E, ?, ?>> capabilities;

        private @Nullable BlockEntityTicker<E> clientTicker;
        private @Nullable BlockEntityTicker<E> serverTicker;

        private BlockEntityTypeBuilder(DeferredHolder<Block, ?> block, BlockEntityType.BlockEntitySupplier<? extends E> factory) {
            this.block = block;
            this.factory = factory;
            this.capabilities = new ArrayList<>();
        }

        public <T, C> BlockEntityTypeBuilder<E> with(BlockCapability<T, C> capability, ICapabilityProvider<? super E, C, T> provider) {
            capabilities.add(new BlockEntityRegistryObject.CapabilityData<>(capability, provider));
            return this;
        }

        public BlockEntityTypeBuilder<E> clientTicker(BlockEntityTicker<E> ticker) {
            Preconditions.checkState(clientTicker == null, "Client ticker may only be set once.");
            clientTicker = ticker;
            return this;
        }

        public BlockEntityTypeBuilder<E> serverTicker(BlockEntityTicker<E> ticker) {
            Preconditions.checkState(serverTicker == null, "Server ticker may only be set once.");
            serverTicker = ticker;
            return this;
        }

        public BlockEntityTypeBuilder<E> commonTicker(BlockEntityTicker<E> ticker) {
            return clientTicker(ticker)
                    .serverTicker(ticker);
        }

        public BlockEntityRegistryObject<E> build() {
            var holder = (BlockEntityRegistryObject<E>) register(block.getId().getPath(),
                    () -> BlockEntityType.Builder.<E>of(factory, block.value()).build(null));
            holder.setCapabilities(capabilities.isEmpty() ? null : capabilities);
            holder.setTickers(clientTicker, serverTicker);
            return holder;
        }
    }
}
