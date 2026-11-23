package io.github.shiozakanaptr.acext.util.function.inttype;

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
public interface IntPredicate extends Predicate<Integer> {
    boolean test(int t);

    default boolean test(Integer t) {
        return this.test(t != null ? t : (int) 0);
    }
}
