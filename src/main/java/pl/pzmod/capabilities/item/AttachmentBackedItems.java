package pl.pzmod.capabilities.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.AttachmentBackedContainer;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.capabilities.ContainerType;
import pl.pzmod.data.containers.IAttachmentHolder;
import pl.pzmod.data.containers.energy.AttachedEnergy;
import pl.pzmod.data.containers.items.AttachedItems;
import pl.pzmod.utils.ConstantPredicates;

import java.util.function.BiFunction;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class AttachmentBackedItems extends AttachmentBackedContainer<ItemStack, AttachedItems> implements IItemContainer {
    public static BiFunction<IAttachmentHolder<AttachedItems>, Integer, AttachmentBackedItems> inout(
            Predicate<@NotNull ItemStack> validator,
            IntSupplier capacity
    ) {
        return (holder, index) ->
                new AttachmentBackedItems(holder, index, validator, ConstantPredicates.alwaysTrue(), ConstantPredicates.alwaysTrue(), capacity);
    }

    public static BiFunction<IAttachmentHolder<AttachedItems>, Integer, AttachmentBackedItems> input(
            Predicate<@NotNull ItemStack> validator,
            IntSupplier capacity
    ) {
        return (holder, index) ->
                new AttachmentBackedItems(holder, index, validator, ConstantPredicates.alwaysTrue(), AutomationType::notExternal, capacity);
    }

    public static BiFunction<IAttachmentHolder<AttachedItems>, Integer, AttachmentBackedItems> output(
            Predicate<@NotNull ItemStack> validator,
            IntSupplier capacity
    ) {
        return (holder, index) ->
                new AttachmentBackedItems(holder, index, validator, AutomationType::notExternal, ConstantPredicates.alwaysTrue(), capacity);
    }

    private final Predicate<@NotNull ItemStack> validator;
    private final Predicate<@NotNull AutomationType> canInsert;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final IntSupplier capacity;

    protected AttachmentBackedItems(@NotNull IAttachmentHolder<AttachedItems> holder,
                                    int index,
                                    Predicate<@NotNull ItemStack> validator,
                                    Predicate<@NotNull AutomationType> canInsert,
                                    Predicate<@NotNull AutomationType> canExtract,
                                    IntSupplier capacity) {
        super(holder, index);
        this.validator = validator;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.capacity = capacity;
    }

    @Override
    protected @NotNull ContainerType<AttachedItems> getContainerType() {
        return ContainerType.ITEMS;
    }

    @Override
    public int getCapacity() {
        return capacity.getAsInt();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return getContents(getAttached());
    }

    @Override
    public void setItem(@NotNull ItemStack stack) {
        setContents(getAttached(), stack);
    }

    @Override
    public boolean isItemValid(@NotNull ItemStack stack) {
        return validator.test(stack);
    }

    @Override
    public boolean canInsert(@NotNull AutomationType automationType) {
        return canInsert.test(automationType);
    }

    @Override
    public boolean canExtract(@NotNull AutomationType automationType) {
        return canExtract.test(automationType);
    }
}
