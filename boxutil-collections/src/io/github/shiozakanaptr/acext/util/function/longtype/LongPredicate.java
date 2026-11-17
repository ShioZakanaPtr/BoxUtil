package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.Predicate;

@FunctionalInterface
public interface LongPredicate extends Predicate<Long> {
    boolean test(long t);

    default boolean test(Long t) {
        return this.test(t != null ? t : (long) 0);
    }
}
