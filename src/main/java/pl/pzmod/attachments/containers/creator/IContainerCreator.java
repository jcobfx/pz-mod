package pl.pzmod.attachments.containers.creator;

import pl.pzmod.attachments.containers.IAttachedContainers;

public interface IContainerCreator<T, A extends IAttachedContainers<T, A>, C> extends IBaseContainerCreator<T, A, C> {
    int totalContainers();

    A initAttached(int size);
}
