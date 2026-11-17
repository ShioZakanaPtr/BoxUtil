package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.Predicate;

@FunctionalInterface
public interface ShortPredicate extends Predicate<Short> {
    boolean test(short t);

    default boolean test(Short t) {
        return this.test(t != null ? t : (short) 0);
    }
}
