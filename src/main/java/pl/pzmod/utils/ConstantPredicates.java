package pl.pzmod.utils;

import java.util.function.Predicate;

public class ConstantPredicates {
    public static <T> Predicate<T> alwaysTrue() {
        return t -> true;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return t -> false;
    }

    private ConstantPredicates() {
    }
}
