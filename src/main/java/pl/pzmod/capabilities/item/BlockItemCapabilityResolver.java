package pl.pzmod.capabilities.item;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import pl.pzmod.capabilities.proxy.PZBlockProxy;
import pl.pzmod.data.containers.AttachedItems;
import pl.pzmod.registries.PZAttachments;

import java.util.function.Predicate;

public class BlockItemCapabilityResolver extends ItemCapabilityResolver<BlockEntity, AttachmentType<AttachedItems>, Direction> {
    public BlockItemCapabilityResolver(BlockEntity blockEntity,
                                       Direction side,
                                       Predicate<Direction> canInsert,
                                       Predicate<Direction> canExtract) {
        super(new PZBlockProxy(blockEntity), PZAttachments.ITEMS_ATTACHMENT, side, canInsert, canExtract);
    }
}
