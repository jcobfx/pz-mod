package pl.pzmod.capabilities.energy;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.attachment.AttachmentType;
import pl.pzmod.blocks.entities.PZBlockEntity;
import pl.pzmod.capabilities.IDataHolder;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZAttachments;

import java.util.function.Predicate;

public class BlockEntityEnergyCapabilityResolver extends EnergyCapabilityResolver<IDataHolder.Block<AttachedEnergy>, AttachmentType<AttachedEnergy>, Direction> {
    public BlockEntityEnergyCapabilityResolver(PZBlockEntity blockEntity, Direction side) {
        this(blockEntity, side, s -> true, s -> true);
    }

    public BlockEntityEnergyCapabilityResolver(PZBlockEntity blockEntity,
                                               Direction side,
                                               Predicate<Direction> canReceive,
                                               Predicate<Direction> canExtract) {
        super(IDataHolder.Block.from(blockEntity), PZAttachments.ENERGY_ATTACHMENT, side, canReceive, canExtract);
    }
}