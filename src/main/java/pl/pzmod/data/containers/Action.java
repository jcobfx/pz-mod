package pl.pzmod.data.containers;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public enum Action {
    EXECUTE(IFluidHandler.FluidAction.EXECUTE),
    SIMULATE(IFluidHandler.FluidAction.SIMULATE);

    private final IFluidHandler.FluidAction fluidAction;

    Action(IFluidHandler.FluidAction fluidAction) {
        this.fluidAction = fluidAction;
    }

    public boolean execute() {
        return this == EXECUTE;
    }

    public boolean simulate() {
        return this == SIMULATE;
    }

    public IFluidHandler.FluidAction toFluidAction() {
        return fluidAction;
    }

    public static Action get(boolean execute) {
        return execute ? EXECUTE : SIMULATE;
    }

    public static Action fromFluidAction(IFluidHandler.FluidAction fluidAction) {
        return fluidAction == IFluidHandler.FluidAction.EXECUTE ? EXECUTE : SIMULATE;
    }
}
