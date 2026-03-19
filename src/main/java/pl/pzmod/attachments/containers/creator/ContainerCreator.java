package pl.pzmod.attachments.containers.creator;

import net.minecraft.world.item.ItemStack;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.attachments.containers.IAttachedContainers;

import java.util.List;

public abstract class ContainerCreator<C, A extends IAttachedContainers<?, A>> implements IContainerCreator<C, A> {
    private final List<IBasicContainerCreator<? extends C>> creators;

    protected ContainerCreator(List<IBasicContainerCreator<? extends C>> creators) {
        this.creators = List.copyOf(creators);
    }

    @Override
    public int totalContainers() {
        return creators.size();
    }

    @Override
    public C create(ContainerType<? super C, ?, ?> containerType, ItemStack attachedTo, int containerIndex) {
        return creators.get(containerIndex).create(containerType, attachedTo, containerIndex);
    }
}
