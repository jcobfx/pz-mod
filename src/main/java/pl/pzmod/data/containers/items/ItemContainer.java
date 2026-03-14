package pl.pzmod.data.containers.items;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.data.containers.IContainerHolder;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.data.containers.BaseContainer;
import pl.pzmod.data.containers.ContainerType;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class ItemContainer extends BaseContainer<ItemStack, AttachedItems> implements IItemContainer {
    private final Predicate<@NotNull ItemStack> validator;
    private final Predicate<@NotNull AutomationType> canInsert;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final IntSupplier capacity;

    public ItemContainer(@NotNull IContainerHolder holder,
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
