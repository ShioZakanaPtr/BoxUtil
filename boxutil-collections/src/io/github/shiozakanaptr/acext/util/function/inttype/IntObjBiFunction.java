package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

/**
 * Type specialization functional interface.
 *
 * @see BiFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface IntObjBiFunction<U, R> extends BiFunction<Integer, U, R> {
    R apply(int t, U u);

    default R apply(Integer t, U u) {
        return this.apply(t != null ? t : (int) 0, u);
    }
}
