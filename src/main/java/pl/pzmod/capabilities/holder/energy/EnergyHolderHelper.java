package pl.pzmod.capabilities.holder.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.capabilities.AutomationType;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.attachments.containers.energy.AttachedEnergyContainer;
import pl.pzmod.attachments.containers.energy.IEnergyContainerCreator;

import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class EnergyHolderHelper {
    public static EnergyHolderHelper forSide(Supplier<Direction> facingSupplier, @NotNull BlockEntity blockEntity) {
        return new EnergyHolderHelper(new EnergyHolder(facingSupplier, blockEntity));
    }

    private final EnergyHolder energyHolder;

    private boolean built;

    private EnergyHolderHelper(EnergyHolder energyHolder) {
        this.energyHolder = energyHolder;
        this.built = false;
    }

    public IEnergyHolder build() {
        checkBuilt();
        built = true;
        return energyHolder;
    }

    public EnergyHolderHelper addBasic(IntSupplier rate, IntSupplier capacity, RelativeSide... sides) {
        return addBasic(AutomationType::manual, ConstantPredicates.alwaysTrue(), rate, capacity, sides);
    }

    public EnergyHolderHelper addBasic(Predicate<@NotNull AutomationType> canExtract,
                                       Predicate<@NotNull AutomationType> canInsert,
                                       IntSupplier maxEnergy,
                                       IntSupplier rate,
                                       RelativeSide... sides) {
        return addStorage((containerIndex, getter, setter, creator) ->
                new AttachedEnergyContainer(containerIndex, getter, setter, creator, canInsert, canExtract, maxEnergy, rate), sides);
    }

    public EnergyHolderHelper addStorage(@NotNull IEnergyContainerCreator<? extends AttachedEnergyContainer> storage, RelativeSide... sides) {
        checkBuilt();
        energyHolder.addEnergyContainer(storage, sides);
        return this;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Holder already built.");
        }
    }

}
