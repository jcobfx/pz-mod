package pl.pzmod.attachments.containers.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.creator.ContainerCreator;
import pl.pzmod.attachments.containers.creator.IBaseContainerCreator;
import pl.pzmod.attachments.containers.ConstantPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemContainersBuilder {
    private static final IItemContainerCreator<? extends AttachedItemContainer> BASIC_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi());
    private static final IItemContainerCreator<? extends AttachedItemContainer> BASIC_INPUT_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal());
    private static final IItemContainerCreator<? extends AttachedItemContainer> OUTPUT_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.internalOnly(), ConstantPredicates.alwaysTrueBi());

    private static final IItemContainerCreator<? extends AttachedItemContainer> FUEL_SLOT_CREATOR =
            (containerIndex, getter, setter, creator) ->
                    new AttachedItemContainer(containerIndex, getter, setter, creator, ConstantPredicates.alwaysTrue(),
                            ConstantPredicates.FUEL_CAN_EXTRACT, ConstantPredicates.FUEL_CAN_INSERT);

    public static ItemContainersBuilder builder() {
        return new ItemContainersBuilder();
    }

    private final List<IBaseContainerCreator<ItemStack, AttachedItems, ? extends AttachedItemContainer>> slotCreators;

    private ItemContainersBuilder() {
        this.slotCreators = new ArrayList<>();
    }

    public ContainerCreator<ItemStack, AttachedItems, AttachedItemContainer> build() {
        return new BaseInventorySlotCreator(slotCreators);
    }

    public ItemContainersBuilder addSlots(int count, IItemContainerCreator<? extends AttachedItemContainer> creator) {
        for (int i = 0; i < count; i++) {
            addSlot(creator);
        }
        return this;
    }

    public ItemContainersBuilder addFuelSlot() {
        return addSlot(FUEL_SLOT_CREATOR);
    }

    public ItemContainersBuilder addOutput() {
        return addSlot(OUTPUT_SLOT_CREATOR);
    }

    public ItemContainersBuilder addOutput(int count) {
        return addSlots(count, OUTPUT_SLOT_CREATOR);
    }

    public ItemContainersBuilder addBasic(int count) {
        return addSlots(count, BASIC_SLOT_CREATOR);
    }

    public ItemContainersBuilder addInput(int count) {
        return addSlots(count, BASIC_INPUT_SLOT_CREATOR);
    }

    public ItemContainersBuilder addInput(Predicate<@NotNull ItemStack> isItemValid) {
        return addSlot((containerIndex, getter, setter, creator) ->
                new AttachedItemContainer(containerIndex, getter, setter, creator, isItemValid,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal()));
    }

    public ItemContainersBuilder addSlot(IItemContainerCreator<? extends AttachedItemContainer> slot) {
        slotCreators.add(slot);
        return this;
    }

    private static class BaseInventorySlotCreator extends ContainerCreator<ItemStack, AttachedItems, AttachedItemContainer> {
        public BaseInventorySlotCreator(List<IBaseContainerCreator<ItemStack, AttachedItems, ? extends AttachedItemContainer>> creators) {
            super(creators);
        }

        @Override
        public AttachedItems initAttached(int containers) {
            return AttachedItems.create(containers);
        }
    }
}
