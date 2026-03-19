package pl.pzmod.attachments.containers.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.creator.ContainerCreator;
import pl.pzmod.attachments.containers.creator.IBasicContainerCreator;
import pl.pzmod.attachments.containers.ConstantPredicates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ItemContainersBuilder {
    private static final IBasicContainerCreator<ComponentBackedItemContainer> BASIC_SLOT_CREATOR =
            (type, attachedTo, containerIndex) -> new ComponentBackedItemContainer(
                    attachedTo, containerIndex, ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrue());
    private static final IBasicContainerCreator<ComponentBackedItemContainer> BASIC_INPUT_SLOT_CREATOR =
            (type, attachedTo, containerIndex) -> new ComponentBackedItemContainer(
                    attachedTo, containerIndex, ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal(), ConstantPredicates.alwaysTrue());
    private static final IBasicContainerCreator<ComponentBackedItemContainer> OUTPUT_SLOT_CREATOR =
            (type, attachedTo, containerIndex) -> new ComponentBackedItemContainer(
                    attachedTo, containerIndex, ConstantPredicates.internalOnly(), ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrue());

    private static final IBasicContainerCreator<ComponentBackedItemContainer> FUEL_SLOT_CREATOR =
            (type, attachedTo, containerIndex) -> new ComponentBackedItemContainer(
                    attachedTo, containerIndex, ConstantPredicates.FUEL_CAN_EXTRACT, ConstantPredicates.FUEL_CAN_INSERT, ConstantPredicates.alwaysTrue());

    public static ItemContainersBuilder builder() {
        return new ItemContainersBuilder();
    }

    private final List<IBasicContainerCreator<? extends ComponentBackedItemContainer>> slotCreators;

    private ItemContainersBuilder() {
        this.slotCreators = new ArrayList<>();
    }

    public ContainerCreator<ComponentBackedItemContainer, AttachedItems> build() {
        return new BaseInventorySlotCreator(slotCreators);
    }

    public ItemContainersBuilder addSlots(int count, IBasicContainerCreator<? extends ComponentBackedItemContainer> creator) {
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
        return addSlot((type, attachedTo, containerIndex) ->
                new ComponentBackedItemContainer(attachedTo, containerIndex, ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal(), isItemValid));
    }

    public ItemContainersBuilder addSlot(IBasicContainerCreator<? extends ComponentBackedItemContainer> slot) {
        slotCreators.add(slot);
        return this;
    }

    private static class BaseInventorySlotCreator extends ContainerCreator<ComponentBackedItemContainer, AttachedItems> {

        public BaseInventorySlotCreator(List<IBasicContainerCreator<? extends ComponentBackedItemContainer>> creators) {
            super(creators);
        }

        @Override
        public AttachedItems initAttached(int containers) {
            return AttachedItems.create(containers);
        }
    }
}
