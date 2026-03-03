package pl.pzmod.capabilities.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import pl.pzmod.capabilities.proxy.PZBlockProxy;
import pl.pzmod.data.containers.AttachedEnergy;
import pl.pzmod.registries.PZAttachments;

import java.util.function.Predicate;

public class BlockEnergyCapabilityResolver extends EnergyCapabilityResolver<BlockEntity, AttachmentType<AttachedEnergy>, Direction> {
    public BlockEnergyCapabilityResolver(BlockEntity blockEntity,
                                         Direction side,
                                         Predicate<Direction> canReceive,
                                         Predicate<Direction> canExtract) {
        super(new PZBlockProxy(blockEntity), PZAttachments.ENERGY_ATTACHMENT, side, canReceive, canExtract);
    }
}