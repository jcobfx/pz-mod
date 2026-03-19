package pl.pzmod.attachments.containers.creator;

import net.minecraft.world.level.block.entity.BlockEntity;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.attachments.containers.IAttachedContainers;

import java.util.function.Supplier;

@FunctionalInterface
public interface IAttachmentBackedContainerCreator<C, A extends IAttachedContainers<?, A>> {
    C create(ContainerType<? super C, A, ?> containerType, BlockEntity attachedTo, int containerIndex, Supplier<A> newAttachmentCreator);
}
