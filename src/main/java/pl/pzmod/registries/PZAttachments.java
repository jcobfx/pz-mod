package pl.pzmod.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import pl.pzmod.PZMod;
import pl.pzmod.data.containers.energy.AttachedEnergy;
import pl.pzmod.data.containers.fluids.AttachedFluids;
import pl.pzmod.data.containers.items.AttachedItems;

public class PZAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PZMod.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedEnergy>> ENERGY_ATTACHMENT =
            ATTACHMENTS.register("energy_attachment", AttachmentType.builder(() -> AttachedEnergy.EMPTY)
                    .serialize(AttachedEnergy.CODEC)
                    .sync(AttachedEnergy.STREAM_CODEC)
                    ::build);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedFluids>> FLUIDS_ATTACHMENT =
            ATTACHMENTS.register("fluids_attachment", AttachmentType.builder(() -> AttachedFluids.EMPTY)
                    .serialize(AttachedFluids.CODEC)
                    .sync(AttachedFluids.STREAM_CODEC)
                    ::build);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AttachedItems>> ITEMS_ATTACHMENT =
            ATTACHMENTS.register("items_attachment", AttachmentType.builder(() -> AttachedItems.EMPTY)
                    .serialize(AttachedItems.CODEC)
                    .sync(AttachedItems.STREAM_CODEC)
                    ::build);

    public static void register(IEventBus bus) {
        ATTACHMENTS.register(bus);
    }

    private PZAttachments() {
    }
}
