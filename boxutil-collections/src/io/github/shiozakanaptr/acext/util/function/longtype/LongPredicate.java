package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.Predicate;

/**
 * Type specialization functional interface.
 *
 * @see Predicate
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface LongPredicate extends Predicate<Long> {
    boolean test(long t);

    default boolean test(Long t) {
        return this.test(t != null ? t : (long) 0);
    }
}
