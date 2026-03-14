package pl.pzmod.data.containers;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public enum AutomationType {
    EXTERNAL,
    INTERNAL,
    MANUAL;

    public static AutomationType handler(@Nullable Direction side) {
        return side == null ? INTERNAL : EXTERNAL;
    }

    public boolean notExternal() {
        return this != EXTERNAL;
    }

    public boolean internal() {
        return this == INTERNAL;
    }

    public boolean manual() {
        return this == MANUAL;
    }
}
