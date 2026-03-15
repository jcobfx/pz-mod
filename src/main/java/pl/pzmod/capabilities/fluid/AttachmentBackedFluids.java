package pl.pzmod.capabilities.fluid;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.AttachmentBackedContainer;
import pl.pzmod.data.containers.AutomationType;
import pl.pzmod.capabilities.ContainerType;
import pl.pzmod.data.containers.IAttachmentHolder;
import pl.pzmod.data.containers.fluids.AttachedFluids;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class AttachmentBackedFluids extends AttachmentBackedContainer<FluidStack, AttachedFluids> implements IFluidContainer {
    private final Predicate<@NotNull FluidStack> validator;
    private final Predicate<@NotNull AutomationType> canExtract;
    private final Predicate<@NotNull AutomationType> canInsert;
    private final IntSupplier capacity;
    private final IntSupplier rate;

    public AttachmentBackedFluids(IAttachmentHolder<AttachedFluids> holder,
                                  int index,
                                  Predicate<@NotNull FluidStack> validator,
                                  Predicate<@NotNull AutomationType> canInsert,
                                  Predicate<@NotNull AutomationType> canExtract,
                                  IntSupplier capacity,
                                  IntSupplier rate) {
        super(holder, index);
        this.validator = validator;
        this.canInsert = canInsert;
        this.canExtract = canExtract;
        this.capacity = capacity;
        this.rate = rate;
    }

    @Override
    protected @NotNull ContainerType<AttachedFluids> getContainerType() {
        return ContainerType.FLUIDS;
    }

    @Override
    public int getRate() {
        return rate.getAsInt();
    }

    @Override
    public int getCapacity() {
        return capacity.getAsInt();
    }

    @Override
    public @NotNull FluidStack getFluid() {
        return getContents(getAttached());
    }

    @Override
    public void setFluid(@NotNull FluidStack fluid) {
        setContents(getAttached(), fluid);
    }

    @Override
    public boolean isFluidValid(@NotNull FluidStack fluid) {
        return validator.test(fluid);
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
