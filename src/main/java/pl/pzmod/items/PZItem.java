package pl.pzmod.items;

import net.minecraft.world.item.Item;
import pl.pzmod.capabilities.energy.IEnergyHolder;

public abstract class PZItem extends Item implements IEnergyHolder {
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
}
