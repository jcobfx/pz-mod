package pl.pzmod.capabilities.item;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.attachment.AttachmentType;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.capabilities.IDataHolder;
import pl.pzmod.data.containers.AttachedItems;
import pl.pzmod.registries.PZAttachments;

import java.util.function.Predicate;

public class BlockItemCapabilityResolver extends ItemCapabilityResolver<IDataHolder.Block<AttachedItems>, AttachmentType<AttachedItems>, Direction> {
    public BlockItemCapabilityResolver(PZBlockEntity blockEntity, Direction side) {
        this(blockEntity, side, s -> true, s -> true);
    }

    public BlockItemCapabilityResolver(PZBlockEntity blockEntity,
                                       Direction side,
                                       Predicate<Direction> canInsert,
                                       Predicate<Direction> canExtract) {
        super(IDataHolder.Block.from(blockEntity), PZAttachments.ITEMS_ATTACHMENT, side, canInsert, canExtract);
    }
}
