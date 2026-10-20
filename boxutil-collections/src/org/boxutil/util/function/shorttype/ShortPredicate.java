package org.boxutil.util.function.shorttype;

import java.util.function.Predicate;

public interface ShortPredicate extends Predicate<Short> {
    boolean test(short t);

    default boolean test(Short t) {
        return this.test(t == null ? (short) 0 : t);
    }
}
