package pl.pzmod.capabilities.holder.item;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.attachments.containers.ConstantPredicates;
import pl.pzmod.attachments.containers.ContainerType;
import pl.pzmod.attachments.containers.creator.IAttachmentBackedContainerCreator;
import pl.pzmod.attachments.containers.item.AttachedItems;
import pl.pzmod.attachments.containers.item.AttachmentBackedItemContainer;
import pl.pzmod.capabilities.RelativeSide;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemContainerHelper {
    private static final IAttachmentBackedContainerCreator<AttachmentBackedItemContainer, AttachedItems> BASIC_SLOT_CREATOR =
            (type, attachedTo, containerIndex, newAttachmentCreator) ->
                    new AttachmentBackedItemContainer(attachedTo, containerIndex, newAttachmentCreator,
                            ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrue());
    private static final IAttachmentBackedContainerCreator<AttachmentBackedItemContainer, AttachedItems> BASIC_INPUT_SLOT_CREATOR =
            (type, attachedTo, containerIndex, newAttachmentCreator) ->
                    new AttachmentBackedItemContainer(attachedTo, containerIndex, newAttachmentCreator,
                            ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal(), ConstantPredicates.alwaysTrue());
    private static final IAttachmentBackedContainerCreator<AttachmentBackedItemContainer, AttachedItems> OUTPUT_SLOT_CREATOR =
            (type, attachedTo, containerIndex, newAttachmentCreator) ->
                    new AttachmentBackedItemContainer(attachedTo, containerIndex, newAttachmentCreator,
                            ConstantPredicates.internalOnly(), ConstantPredicates.alwaysTrueBi(), ConstantPredicates.alwaysTrue());

    private static final IAttachmentBackedContainerCreator<AttachmentBackedItemContainer, AttachedItems> FUEL_SLOT_CREATOR =
            (type, attachedTo, containerIndex, newAttachmentCreator) ->
                    new AttachmentBackedItemContainer(attachedTo, containerIndex, newAttachmentCreator,
                            ConstantPredicates.FUEL_CAN_EXTRACT, ConstantPredicates.FUEL_CAN_INSERT, ConstantPredicates.alwaysTrue());


    public static ItemContainerHelper forSide(Supplier<Direction> facingSupplier, @NotNull BlockEntity blockEntity) {
        return new ItemContainerHelper(new ItemHolder(facingSupplier), blockEntity);
    }

    private final ItemHolder itemHolder;
    private final BlockEntity blockEntity;

    private int currentIndex;
    private boolean built;

    private ItemContainerHelper(ItemHolder itemHolder, BlockEntity blockEntity) {
        this.itemHolder = itemHolder;
        this.blockEntity = blockEntity;
        this.currentIndex = 0;
        this.built = false;
    }

    public IItemHolder build() {
        checkBuilt();
        built = true;
        return itemHolder;
    }

    public ItemContainerHelper addFuelSlot(RelativeSide... sides) {
        return addSlot(FUEL_SLOT_CREATOR, sides);
    }

    public ItemContainerHelper addOutput(RelativeSide... sides) {
        return addSlot(OUTPUT_SLOT_CREATOR, sides);
    }

    public ItemContainerHelper addOutput(int count, List<RelativeSide>... sides) {
        return addSlots(count, OUTPUT_SLOT_CREATOR, sides);
    }

    public ItemContainerHelper addBasic(int count, List<RelativeSide>... sides) {
        return addSlots(count, BASIC_SLOT_CREATOR, sides);
    }

    public ItemContainerHelper addInput(int count, List<RelativeSide>... sides) {
        return addSlots(count, BASIC_INPUT_SLOT_CREATOR, sides);
    }

    public ItemContainerHelper addInput(Predicate<@NotNull ItemStack> isItemValid, RelativeSide... sides) {
        return addSlot((type, attachedTo, containerIndex, newAttachmentCreator) ->
                new AttachmentBackedItemContainer(attachedTo, containerIndex, newAttachmentCreator,
                        ConstantPredicates.alwaysTrueBi(), ConstantPredicates.notExternal(), isItemValid), sides);
    }

    @SafeVarargs
    public final ItemContainerHelper addSlots(int count,
                                              IAttachmentBackedContainerCreator<? extends AttachmentBackedItemContainer, AttachedItems> creator,
                                              List<RelativeSide>... sides) {
        int len = sides.length;
        for (int i = 0; i < count; i++) {
            if (i < len) {
                addSlot(creator, sides[i].toArray(new RelativeSide[0]));
            } else {
                addSlot(creator);
            }
        }
        return this;
    }

    public ItemContainerHelper addSlot(@NotNull IAttachmentBackedContainerCreator<? extends AttachmentBackedItemContainer, AttachedItems> slot,
                                       RelativeSide... sides) {
        checkBuilt();
        var container = slot.create(ContainerType.ITEMS, blockEntity, currentIndex++, itemHolder::initAttachedItems);
        itemHolder.addItemContainer(container, sides);
        return this;
    }

    private void checkBuilt() {
        if (built) {
            throw new IllegalStateException("Holder already built.");
        }
    }
}
