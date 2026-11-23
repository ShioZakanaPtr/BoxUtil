package io.github.shiozakanaptr.acext.util.function.doubletype;

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
public interface DoublePredicate extends Predicate<Double> {
    boolean test(double t);

    default boolean test(Double t) {
        return this.test(t != null ? t : (double) 0);
    }
}
