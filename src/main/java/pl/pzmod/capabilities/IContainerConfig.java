package pl.pzmod.capabilities;

import pl.pzmod.data.containers.IContainerHolder;

public interface IContainerConfig<T> {
    T createContainer(IContainerHolder holder, int index);
}
