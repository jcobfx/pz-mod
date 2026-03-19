package pl.pzmod.attachments.containers;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.pzmod.capabilities.AutomationType;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class ConstantPredicates {
    public static final BiPredicate<@NotNull ItemStack, @NotNull AutomationType> FUEL_CAN_EXTRACT =
            (stack, automationType) -> automationType.manual() || stack.getBurnTime(null) == 0;
    public static final BiPredicate<@NotNull ItemStack, @NotNull AutomationType> FUEL_CAN_INSERT =
            (stack, automationType) -> stack.getBurnTime(null) != 0;

    private static final Predicate<?> ALWAYS_TRUE = t -> true;
    private static final BiPredicate<?, ?> ALWAYS_TRUE_BI = (t, r) -> true;

    private static final BiPredicate<?, @NotNull AutomationType> NOT_EXTERNAL =
            (t, automationType) -> automationType.notExternal();
    private static final BiPredicate<?, @NotNull AutomationType> INTERNAL_ONLY =
            (t, automationType) -> automationType.internal();
    private static final BiPredicate<?, @NotNull AutomationType> MANUAL_ONLY =
            (t, automationType) -> automationType.manual();

    public static <T> Predicate<T> alwaysTrue() {
        return (Predicate<T>) ALWAYS_TRUE;
    }

    public static <T, R> BiPredicate<T, R> alwaysTrueBi() {
        return (BiPredicate<T, R>) ALWAYS_TRUE_BI;
    }

    public static <T> BiPredicate<T, @NotNull AutomationType> notExternal() {
        return (BiPredicate<T, @NotNull AutomationType>) NOT_EXTERNAL;
    }

    public static <T> BiPredicate<T, @NotNull AutomationType> internalOnly() {
        return (BiPredicate<T, @NotNull AutomationType>) INTERNAL_ONLY;
    }

    public static <T> BiPredicate<T, @NotNull AutomationType> manualOnly() {
        return (BiPredicate<T, @NotNull AutomationType>) MANUAL_ONLY;
    }

    private ConstantPredicates() {
    }
}
