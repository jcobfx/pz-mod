package pl.pzmod.attachments.containers.creator;

import pl.pzmod.attachments.containers.IAttachedContainers;

public interface IContainerCreator<C, A extends IAttachedContainers<?, A>> extends IBasicContainerCreator<C> {
    int totalContainers();

    A initAttached(int size);
}
