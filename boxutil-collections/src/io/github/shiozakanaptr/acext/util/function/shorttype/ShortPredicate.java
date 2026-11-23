package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortPredicate extends Predicate<Short> {
    boolean test(short t);

    default boolean test(Short t) {
        return this.test(t != null ? t : (short) 0);
    }
}
