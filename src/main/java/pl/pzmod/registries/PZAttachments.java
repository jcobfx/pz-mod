package pl.pzmod.registries;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import pl.pzmod.PZMod;
import pl.pzmod.attachments.fluids.AttachedFluids;
import pl.pzmod.attachments.items.AttachedItems;

import java.util.function.Supplier;

public class PZAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PZMod.MODID);

    public static final Supplier<AttachmentType<Integer>> ENERGY_CONTAINER =
            ATTACHMENT_TYPES.register("energy_container", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .sync(ByteBufCodecs.INT)
                    .build());
    public static final Supplier<AttachmentType<AttachedFluids>> FLUID_CONTAINER =
            ATTACHMENT_TYPES.register("fluid_container", () -> AttachmentType.builder(() -> AttachedFluids.EMPTY)
                    .serialize(AttachedFluids.CODEC)
                    .sync(AttachedFluids.STREAM_CODEC)
                    .build());
    public static final Supplier<AttachmentType<AttachedItems>> ITEM_CONTAINER =
            ATTACHMENT_TYPES.register("item_container", () -> AttachmentType.builder(() -> AttachedItems.EMPTY)
                    .serialize(AttachedItems.CODEC)
                    .sync(AttachedItems.STREAM_CODEC)
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }

    private PZAttachments() {
    }
}
