package pl.pzmod.items;

import net.minecraft.world.item.Item;
import pl.pzmod.capabilities.energy.IEnergyHolder;
import pl.pzmod.capabilities.item.IItemHolder;

public abstract class PZItem extends Item implements IEnergyHolder, IItemHolder {
    public PZItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getEnergyCapacity() {
        return 0;
    }

    @Override
    public int getEnergyMaxTransfer() {
        return 0;
    }

    @Override
    public int getSlotCount() {
        return 0;
    }

    @Override
    public int getSlotLimit() {
        return 0;
    }
}
