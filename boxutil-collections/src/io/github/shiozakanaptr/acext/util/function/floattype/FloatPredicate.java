package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatPredicate extends Predicate<Float> {
    boolean test(float t);

    default boolean test(Float t) {
        return this.test(t != null ? t : (float) 0);
    }
}
