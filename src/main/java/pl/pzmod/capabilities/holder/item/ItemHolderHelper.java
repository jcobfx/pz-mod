package pl.pzmod.capabilities.holder.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.capabilities.RelativeSide;
import pl.pzmod.attachments.containers.item.AttachedItemContainer;
import pl.pzmod.attachments.containers.item.IItemContainerCreator;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemHolderHelper {
    private static final IItemContainerCreator<AttachedItemContainer> BASIC_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi());
    private static final IItemContainerCreator<AttachedItemContainer> BASIC_INPUT_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal());
    private static final IItemContainerCreator<AttachedItemContainer> OUTPUT_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.internalOnly(), ConstantPredicates.alwaysTrueBi());

    private static final IItemContainerCreator<AttachedItemContainer> FUEL_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.FUEL_CAN_INSERT, ConstantPredicates.FUEL_CAN_EXTRACT);


    public static ItemHolderHelper forSide(Supplier<Direction> facingSupplier, @NotNull BlockEntity blockEntity) {
        return new ItemHolderHelper(new ItemHolder(facingSupplier, blockEntity));
    }

    private final ItemHolder holder;

    private boolean built;

    private ItemHolderHelper(ItemHolder holder) {
        this.holder = holder;
        this.built = false;
    }

    public IItemHolder build() {
        checkBuilt();
        built = true;
        return holder;
    }

    public ItemHolderHelper addFuelSlot(RelativeSide... sides) {
        return addSlot(FUEL_SLOT_CREATOR, sides);
    }

    public ItemHolderHelper addOutput(RelativeSide... sides) {
        return addSlot(OUTPUT_SLOT_CREATOR, sides);
    }

    public ItemHolderHelper addOutput(int count, RelativeSide... sides) {
        return addSlots(count, OUTPUT_SLOT_CREATOR, sides);
    }

    public ItemHolderHelper addBasic(int count, RelativeSide... sides) {
        return addSlots(count, BASIC_SLOT_CREATOR, sides);
    }

    public ItemHolderHelper addInput(int count, RelativeSide... sides) {
        return addSlots(count, BASIC_INPUT_SLOT_CREATOR, sides);
    }

    public ItemHolderHelper addInput(Predicate<@NotNull ItemStack> isItemValid, RelativeSide... sides) {
        return addSlot((containerIndex, getter, setter, creator) ->
                new AttachedItemContainer(containerIndex, getter, setter, creator, isItemValid,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal()), sides);
    }

    public ItemHolderHelper addSlots(int count,
                                     @NotNull IItemContainerCreator<? extends AttachedItemContainer> creator,
                                     RelativeSide... sides) {
        for (int i = 0; i < count; i++) {
            addSlot(creator, sides);
        }
        return this;
    }

    public ItemHolderHelper addSlot(@NotNull IItemContainerCreator<? extends AttachedItemContainer> slot, RelativeSide... sides) {
        checkBuilt();
        holder.addItemContainer(slot, sides);
        return this;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Holder already built.");
        }
    }
}
